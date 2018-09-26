package com.hobbajt.bubblequiz.levelpacks.view

import android.util.Log
import com.hobbajt.bubblequiz.levelpacks.model.LevelPacksApiLoader
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.sharedprefs.SharedPreferencesEditor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class LevelPacksPresenter @Inject constructor(private val levelPacksApiLoader: LevelPacksApiLoader,
                                              private val sharedPreferencesEditor: SharedPreferencesEditor) : BasePresenter<LevelPacksContractor.View>()
{
    private val compositeDisposable = CompositeDisposable()
    private var levelsPacks = emptyList<LevelsPack>()
    private var passedLevelsTotalCount = 0

    override fun detachView()
    {
        compositeDisposable.clear()
    }

    fun onRetryButtonClicked()
    {
        loadLevelPacks()
    }

    fun onViewCreated(levelsPacks: List<LevelsPack>?)
    {
        passedLevelsTotalCount = sharedPreferencesEditor.readTotalPassedLevelsCount()
        if(levelsPacks != null)
        {
            this.levelsPacks = levelsPacks
            view?.displayData()
        }
        else
        {
            loadLevelPacks()
        }
    }

    private fun loadLevelPacks()
    {
        compositeDisposable.add(levelPacksApiLoader.load()
                .subscribeOn(Schedulers.io())
                .retry(3)
                .map { items ->
                    if (items.isEmpty())
                    {
                        throw IOException()
                    } else
                    {
                        items
                    }
                }
                .flatMapIterable { items -> items }
                .map { it.passedLevels = sharedPreferencesEditor.readPassedLevelsInPack(it.id).size
                    it}
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items ->
                            levelsPacks = items
                            view?.displayData()
                        },
                        { exception ->
                            Log.e("error", exception.message)
                            view?.displayDownloadErrorDialog()
                        }))
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
