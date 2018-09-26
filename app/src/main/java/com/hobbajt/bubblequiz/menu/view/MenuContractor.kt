package com.hobbajt.bubblequiz.menu.view


interface MenuContractor
{
    interface View
    {
        fun displayUI()

        fun openOptions()

        fun openLevelPacks()
    }
}
