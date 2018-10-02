package com.hobbajt.bubblequiz.photo.view

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.photo.model.BubblesSet

interface PhotoContract
{
    interface View
    {
        fun disableAllHintsAnimations()

        fun displayNewLevelPackUnlockedDialog()

        fun displayPointsCount(pointsCount : Int)

        fun displayDownloadErrorDialog(message: String)

        fun hideLoader()

        fun displayAnswerFieldEmpty(answer: String)

        fun displayPhoto(photoUrl: String, screenSize: Int)

        fun displayCorrectAnswer()

        fun hidePlayUI()

        fun createListeners()

        fun openLevel(levelsPack: LevelsPack, levelId: Int)

        fun hideKeyboard()

        fun displayBubblesSurface(drawItems: BubblesSet, surfaceSize: Int)

        fun displayAvailableMovesBar(maxSplits: Float)

        fun animateBombHint()

        fun animateShowPartHint()

        fun hideAnswerLetters()

        fun displayAvailableMoves(movePoints: Float)

        fun backToLevels()

        fun loadImage(photoUrl: String, size: Int, listener: OnImageLoadListener)

        fun hidePhoto()

        fun hideSurface()

        fun displayLoader()
    }
}
