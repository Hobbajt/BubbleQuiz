package com.hobbajt.bubblequiz.menu.di

import com.hobbajt.bubblequiz.application.di.scopes.FragmentScope
import com.hobbajt.bubblequiz.menu.view.MenuFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [MenuModule::class])
interface MenuComponent
{
    fun inject(target: MenuFragment)
}