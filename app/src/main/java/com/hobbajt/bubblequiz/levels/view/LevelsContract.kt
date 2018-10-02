package com.hobbajt.bubblequiz.levels.view

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack

interface LevelsContract
{
    interface View
    {
        fun displayData()

        fun openPhoto(levelsPack: LevelsPack, position: Int)
    }
}
