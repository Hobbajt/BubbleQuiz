package com.hobbajt.bubblequiz.levelpacks.view

interface LevelPackHolderView
{
    fun bind()

    fun displayDefault()

    fun displayComplete()

    fun displayUnlocked(passedLevels: Int, levelsCount: Int)

    fun displayLocked(requiredLevelsCount: Int, passedLevelsCount: Int)

    fun displayLockedAnimation()

    fun displayUnlockedAnimation()
}