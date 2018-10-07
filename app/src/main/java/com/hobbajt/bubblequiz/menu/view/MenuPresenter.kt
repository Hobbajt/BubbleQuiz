package com.hobbajt.bubblequiz.menu.view

import com.hobbajt.bubblequiz.mvp.BasePresenter

class MenuPresenter : BasePresenter<MenuContract.View>()
{
    fun onViewCreated()
    {
        view?.displayUI()
    }

    fun onPlayClicked()
    {
        view?.openLevelPacks()
    }

    fun onOptionsClicked()
    {
        view?.openOptions()
    }
}
