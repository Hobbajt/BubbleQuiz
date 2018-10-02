package com.hobbajt.bubblequiz.levels.di

import com.hobbajt.bubblequiz.levels.view.LevelsPresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import dagger.Module
import dagger.Provides

@Module
class LevelsModule
{
    @Provides
    fun providesLevelsPresenter(localDataEditor: LocalDataEditor):
            LevelsPresenter = LevelsPresenter(localDataEditor)
}