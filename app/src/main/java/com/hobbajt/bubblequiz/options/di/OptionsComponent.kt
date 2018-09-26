package com.hobbajt.bubblequiz.options.di

import com.hobbajt.bubblequiz.application.di.scopes.FragmentScope
import com.hobbajt.bubblequiz.options.view.OptionsFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [OptionsModule::class])
interface OptionsComponent
{
    fun inject(target: OptionsFragment)
}