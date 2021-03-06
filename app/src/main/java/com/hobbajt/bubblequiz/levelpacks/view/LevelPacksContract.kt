package com.hobbajt.bubblequiz.levelpacks.view

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack

interface LevelPacksContract
{
    interface View
    {
        fun displayData()

        fun displayDownloadErrorDialog()

        fun openLevels(levelPack: LevelsPack)
    }
}
