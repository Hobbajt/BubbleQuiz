package com.hobbajt.bubblequiz.options.di

import com.hobbajt.bubblequiz.options.view.OptionsPresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import dagger.Module
import dagger.Provides

@Module
class OptionsModule
{
    @Provides
    fun providesOptionsPresenter(localDataEditor: LocalDataEditor): OptionsPresenter = OptionsPresenter(localDataEditor)
}