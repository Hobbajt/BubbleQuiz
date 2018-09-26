package com.hobbajt.bubblequiz.levels.view

interface LevelHolderView
{
    fun bind(position: Int, isLevelPassed: Boolean, presenter: LevelsPresenter)

    fun displayOpenAnimation()
}