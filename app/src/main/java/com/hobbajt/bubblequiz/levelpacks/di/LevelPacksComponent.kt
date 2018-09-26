package com.hobbajt.bubblequiz.levelpacks.di

import com.hobbajt.bubblequiz.application.di.scopes.FragmentScope
import com.hobbajt.bubblequiz.levelpacks.view.LevelPacksFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [LevelPacksModule::class])
interface LevelPacksComponent
{
    fun inject(target: LevelPacksFragment)
}