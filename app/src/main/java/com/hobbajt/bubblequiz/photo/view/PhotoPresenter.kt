package com.hobbajt.bubblequiz.photo.view

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.levels.model.dto.Level
import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.photo.model.*
import com.hobbajt.bubblequiz.photo.model.dto.LevelState
import com.hobbajt.bubblequiz.photo.model.dto.Position
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.math.NumberUtils
import java.util.*
import javax.inject.Inject


class PhotoPresenter @Inject constructor(private val localDataEditor: LocalDataEditor,
                                         private val photoApiLoader: PhotoApiLoader,
                                         private val language: String,
                                         private val screenSize: Int) :
        BasePresenter<PhotoContract.View>()
{
    companion object
    {
        private const val MAX_SPLITS = 1400
    }

    private val compositeDisposable = CompositeDisposable()

    private lateinit var levelsPack: LevelsPack
    private lateinit var level: Level
    private var levelState: LevelState? = null
    private var levelId: Int = -1

    private lateinit var bubblesProcessor: BubblesProcessor

    private var availableSplits: Int = MAX_SPLITS
    private var restartCount: Int = 0
    private var currentHintType: HintType? = null



    enum class HintType(val requiredPoints: Int)
    {
        BOMB(2),
        SHOW_PART(4)
    }

    enum class BubblesTouchType(val id: Int)
    {
        SWIPE(2),
        TOUCH(0);

        companion object
        {
            fun getTypeFromId(id: Int): BubblesTouchType
            {
                for (value in values())
                {
                    if (id == value.id)
                        return value
                }
                return SWIPE
            }
        }
    }

    fun onViewCreated(levelsPack: LevelsPack?, levelId: Int?, levelState: LevelState?)
    {
        view?.displayLoader()
        if (levelsPack != null && levelId != null)
        {
            this.levelsPack = levelsPack
            this.levelId = levelId
            this.levelState = levelState

            loadLevel(levelId)
        } else
        {
            view?.backToLevels()
        }
    }

    // region Load Level
    private fun loadLevel(levelId: Int)
    {
        compositeDisposable.add(photoApiLoader.load(levelsPack.id, levelId, language, screenSize)
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item -> onLevelLoadComplete(item) },
                        { exception -> onLevelLoadFailed(exception) }))
    }

    private fun onLevelLoadFailed(exception: Throwable)
    {
        view?.displayDownloadErrorDialog(exception.message ?: "")
    }

    private fun onLevelLoadComplete(item: Level)
    {
        level = item
        loadImage()
    }

    private fun loadImage()
    {
        view?.loadImage(level.photoUrl, screenSize, object : OnImageLoadListener
        {
            override fun onSuccess(imageBytes: ByteArray)
            {
                bubblesProcessor = BubblesProcessor(imageBytes, screenSize)
                levelState?.let { levelState ->
                    availableSplits = levelState.availableSplits
                    restartCount = levelState.restartCount
                    levelState.bubblesState?.let {
                        bubblesProcessor.setBubblesState(it)
                    }
                }

                displayUI(bubblesProcessor.getBubblesSet())
            }

            override fun onFailed(exception: Throwable)
            {
                view?.backToLevels()
            }
        })
    }
    // endregion

    // region Display UI
    private fun displayUI(bubblesSet: BubblesSet)
    {
        view?.createListeners()
        view?.hideLoader()
        view?.displayPointsCount(localDataEditor.readPointsCount())
        view?.displayAnswerFieldEmpty(level.answer)

        if (localDataEditor.isLevelPassed(level))
        {
            displayPassedUI(level.photoUrl)
        } else
        {
            view?.hidePhoto()
            view?.displayAvailableMovesBar(MAX_SPLITS.toFloat())
            view?.displayAvailableMoves(availableSplits.toFloat())
            view?.displayBubblesSurface(bubblesSet, bubblesProcessor.getSurfaceSize())
        }
    }

    private fun displayPassedUI(photoUrl: String)
    {
        view?.hidePlayUI()
        view?.hideKeyboard()
        view?.displayPhoto(photoUrl, screenSize)
        view?.displayCorrectAnswer()
    }
    // endregion

    private fun savePassedLevel()
    {
        localDataEditor.writePassedLevel(level)
    }

    private fun openNextLevel()
    {
        view?.openLevel(levelsPack, getNextUndoneLevelId(levelsPack, level.id))
    }

    private fun getNextUndoneLevelId(levelsPack: LevelsPack, currentLevelId: Int): Int
    {
        val passedLevelsInPack = localDataEditor.readPassedLevelsInPack(levelsPack.id)
        val passedLevels: MutableSet<Int> = TreeSet(passedLevelsInPack)

        for (i in 0 until levelsPack.levelsCount)
        {
            if (i > currentLevelId && !passedLevels.contains(i))
                return i
        }

        if (currentLevelId + 1 < levelsPack.levelsCount)
            return currentLevelId + 1

        return 0
    }



    // region Answer
    fun onAnswerWriteComplete(signs: String?)
    {
        if (signs != null && AnswerChecker.isAnswerCorrect(signs, level.answer))
        {
            savePassedLevel()

            //App.decrementRemainingLevelsCount()
            val points = NumberUtils.max(1, 3 - restartCount)
            val totalPoints = localDataEditor.writePointsCount(points)
            view?.displayPointsCount(totalPoints)
            displayPassedUI(level.photoUrl)
        }
    }
    // endregion

    // region UI Clicks
    fun onLoadRetryClicked()
    {
        loadLevel(levelId)
    }

    fun onLoadCancelClicked()
    {
        view?.backToLevels()
    }

    fun onShowPartClicked()
    {
        onHintClicked(HintType.SHOW_PART)
    }

    fun onBombClicked()
    {
        onHintClicked(HintType.BOMB)
    }

    fun onNextClicked()
    {
        view?.hideKeyboard()
        openNextLevel()
    }

    fun onRestartClicked()
    {
        restartCount++

        view?.hideAnswerLetters()
        view?.displayAvailableMoves(MAX_SPLITS.toFloat())

        val bubblesSet = bubblesProcessor.getBubblesSet()
        displayUI(bubblesSet)
    }

    private fun onHintClicked(selectedHintType: HintType)
    {
        if (selectedHintType != currentHintType && localDataEditor.readPointsCount() > selectedHintType.requiredPoints)
        {
            currentHintType = selectedHintType

            when (currentHintType)
            {
                HintType.BOMB -> view?.animateBombHint()
                HintType.SHOW_PART -> view?.animateShowPartHint()
            }
        } else
        {
            view?.disableAllHintsAnimations()
        }
    }
    // endregion

    // region Surface Events
    fun onBubbleTouch(x: Float, y: Float, touchType: Int): Boolean
    {
        val bubblePosition = bubblesProcessor.convertPositionPxToBubblePosition(x, y)
        if (bubblePosition.x in 0..63 && bubblePosition.y in 0..63)
        {
            when (BubblesTouchType.getTypeFromId(touchType))
            {
                BubblesTouchType.SWIPE -> onSurfaceSwipe(bubblePosition)
                BubblesTouchType.TOUCH -> currentHintType?.let { onSurfaceHint(bubblePosition) }
                        ?: onSurfaceSwipe(bubblePosition)
            }
        }
        return true
    }

    private fun onSurfaceSwipe(bubblePosition: Position)
    {
        availableSplits = bubblesProcessor.useSwipe(bubblePosition, availableSplits)
        view?.displayAvailableMoves(availableSplits.toFloat())
    }

    private fun onSurfaceHint(bubblePosition: Position)
    {
        if (localDataEditor.readPointsCount() > 0)
        {
            val thread = Thread {

                val start = PositionCalculator.matchStartPositionOfHint(bubblePosition)

                when (currentHintType)
                {
                    HintType.BOMB -> useBomb(start, HintType.BOMB.requiredPoints)
                    HintType.SHOW_PART -> usePhotoPart(start, HintType.SHOW_PART.requiredPoints)
                }
            }
            thread.start()
            try
            {
                thread.join()
                currentHintType = null
                view?.disableAllHintsAnimations()
                view?.displayPointsCount(localDataEditor.readPointsCount())
            } catch (e: InterruptedException)
            {
                e.printStackTrace()
            }
        }
    }

    private fun useBomb(start: Position, requiredPoints: Int)
    {
        compositeDisposable.add(
                Observable.fromCallable { bubblesProcessor.useBomb(start) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isUsed ->
                            onHintUseComplete(isUsed, requiredPoints)
                        })
    }

    private fun usePhotoPart(start: Position, requiredPoints: Int)
    {
        compositeDisposable.add(
                Observable.fromCallable { bubblesProcessor.usePhotoPart(start) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { isUsed ->
                            onHintUseComplete(isUsed, requiredPoints)
                        })
    }

    private fun onHintUseComplete(isUsed: Boolean, requiredPoints: Int)
    {
        if (isUsed)
        {
            val points = localDataEditor.writePointsCount(-requiredPoints)
            view?.displayPointsCount(points)
        }
    }
    // endregion

    fun onSaveState() = LevelState(bubblesProcessor.getBubblesState(), availableSplits, restartCount)
}