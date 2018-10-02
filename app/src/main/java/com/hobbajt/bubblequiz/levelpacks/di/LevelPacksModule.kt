package com.hobbajt.bubblequiz.levelpacks.di

import com.hobbajt.bubblequiz.application.Api
import com.hobbajt.bubblequiz.levelpacks.model.LevelPacksApiLoader
import com.hobbajt.bubblequiz.levelpacks.view.LevelPacksPresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import dagger.Module
import dagger.Provides

@Module
class LevelPacksModule
{
    @Provides
    fun providesLevelPacksApiLoader(api: Api): LevelPacksApiLoader = LevelPacksApiLoader(api)

    @Provides
    fun providesLevelPacksPresenter(levelPacksApiLoader: LevelPacksApiLoader, localDataEditor: LocalDataEditor):
            LevelPacksPresenter = LevelPacksPresenter(levelPacksApiLoader, localDataEditor)


}