package com.hobbajt.bubblequiz.levelpacks.view

import com.hobbajt.bubblequiz.levelpacks.model.LevelPacksApiLoader
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LevelPacksPresenter @Inject constructor(private val levelPacksApiLoader: LevelPacksApiLoader,
                                              private val localDataEditor: LocalDataEditor) : BasePresenter<LevelPacksContract.View>()
{
    private val compositeDisposable = CompositeDisposable()
    private var levelsPacks = emptyList<LevelsPack>()
    private var passedLevelsTotalCount = 0

    override fun detachView()
    {
        super.detachView()
        compositeDisposable.clear()
    }

    fun onRetryButtonClicked()
    {
        loadLevelPacks()
    }

    fun onViewCreated(levelsPacks: List<LevelsPack>?)
    {
        loadTotalPassedLevelsCount()
        if(levelsPacks != null)
        {
            onLoadLevelPacksSuccess(levelsPacks)
        }
        else
        {
            loadLevelPacks()
        }
    }

    fun loadTotalPassedLevelsCount()
    {
        passedLevelsTotalCount = localDataEditor.readTotalPassedLevelsCount()
    }

    fun loadLevelPacks()
    {
        compositeDisposable.add(levelPacksApiLoader.load()
                .subscribeOn(Schedulers.io())
                .retry(3)
                .doOnNext()
                {
                    require(it.isNotEmpty())
                    {
                        "API returned no level packs!"
                    }
                }

                .flatMapIterable { items -> items }
                .map
                {
                    val passedLevelsCount = localDataEditor.readPassedLevelsInPack(it.id).size
                    LevelsPack(it.id, it.levelsCount, it.requiredPassedLevels, passedLevelsCount)
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onLoadLevelPacksSuccess(it) }, { onLoadLevelPacksError()}))
    }

    fun onLoadLevelPacksError()
    {
        view?.displayDownloadErrorDialog()
    }

    fun onLoadLevelPacksSuccess(items: List<LevelsPack>)
    {
        levelsPacks = items
        view?.displayData()
    }

    fun onCancelButtonClicked()
    {
        System.exit(0)
    }

    fun onBindLevelPackViewAtPosition(itemView: LevelPackHolderView, position: Int)
    {
        val levelPackInfo = levelsPacks[position]

        itemView.bind()
        itemView.displayDefault()

        if (levelPackInfo.levelsCount == levelPackInfo.passedLevels)
            itemView.displayComplete()
        else if (passedLevelsTotalCount >= levelPackInfo.requiredPassedLevels)
            itemView.displayUnlocked(levelPackInfo.passedLevels, levelPackInfo.levelsCount)
        else
            itemView.displayLocked(levelPackInfo.requiredPassedLevels, passedLevelsTotalCount)
    }

    fun getItemsCount() = levelsPacks.size

    fun onLevelPackClicked(position: Int, itemView: LevelPackHolderView)
    {
        val levelPackInfo = levelsPacks[position]

        if (passedLevelsTotalCount < levelPackInfo.requiredPassedLevels)
        {
            itemView.displayLockedAnimation()
        }
        else
        {
            itemView.displayUnlockedAnimation()
            view?.openLevels(levelPackInfo)
        }
    }

    fun onSaveState() = levelsPacks
}
