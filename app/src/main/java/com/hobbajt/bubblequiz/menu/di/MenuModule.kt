package com.hobbajt.bubblequiz.menu.di

import com.hobbajt.bubblequiz.menu.view.MenuPresenter
import dagger.Module
import dagger.Provides

@Module
class MenuModule
{
    @Provides
    fun providesMenuPresenter(): MenuPresenter = MenuPresenter()
}