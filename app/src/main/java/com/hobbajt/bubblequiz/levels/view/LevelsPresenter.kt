package com.hobbajt.bubblequiz.levels.view

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.mvp.BasePresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import javax.inject.Inject

class LevelsPresenter @Inject constructor(private val localDataEditor: LocalDataEditor):
        BasePresenter<LevelsContract.View>()
{
    private var passedLevels: Set<Int> = emptySet()
    private var levelsPack: LevelsPack? = null

    fun onCreateView(levelsPack: LevelsPack)
    {
        this.levelsPack = levelsPack
        passedLevels = localDataEditor.readPassedLevelsInPack(levelsPack.id)
        view?.displayData()
    }

    fun getItemCount() = levelsPack?.levelsCount ?: 0

    fun onBindLevelViewAtPosition(itemView: LevelHolderView, position: Int)
    {
        val isLevelPassed = passedLevels.contains(position)
        itemView.bind(position, isLevelPassed, this)
    }

    fun onLevelClicked(position: Int, itemView: LevelHolderView)
    {
        itemView.displayOpenAnimation()
        levelsPack?.let {
            view?.openPhoto(it, position)
        }
    }
}