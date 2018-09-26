package com.hobbajt.bubblequiz.levels.di

import com.hobbajt.bubblequiz.levels.view.LevelsPresenter
import com.hobbajt.bubblequiz.sharedprefs.SharedPreferencesEditor
import dagger.Module
import dagger.Provides

@Module
class LevelsModule
{
    @Provides
    fun providesLevelsPresenter(sharedPreferencesEditor: SharedPreferencesEditor):
            LevelsPresenter = LevelsPresenter(sharedPreferencesEditor)
}