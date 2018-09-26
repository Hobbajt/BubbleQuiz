package com.hobbajt.bubblequiz.menu.view

import com.hobbajt.bubblequiz.mvp.BasePresenter
import javax.inject.Inject

class MenuPresenter @Inject constructor(): BasePresenter<MenuContractor.View>()
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
