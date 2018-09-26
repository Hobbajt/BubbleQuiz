package com.hobbajt.bubblequiz.options.di

import com.hobbajt.bubblequiz.options.view.OptionsPresenter
import com.hobbajt.bubblequiz.sharedprefs.SharedPreferencesEditor
import dagger.Module
import dagger.Provides

@Module
class OptionsModule
{
    @Provides
    fun providesOptionsPresenter(sharedPreferencesEditor: SharedPreferencesEditor): OptionsPresenter = OptionsPresenter(sharedPreferencesEditor)
}