package com.hobbajt.bubblequiz.levels.di

import com.hobbajt.bubblequiz.application.di.scopes.FragmentScope
import com.hobbajt.bubblequiz.levels.view.LevelsFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [LevelsModule::class])
interface LevelsComponent
{
    fun inject(target: LevelsFragment)
}