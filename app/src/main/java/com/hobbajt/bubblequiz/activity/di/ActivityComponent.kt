package com.hobbajt.bubblequiz.activity.di

import com.hobbajt.bubblequiz.application.di.scopes.ActivityScope
import com.hobbajt.bubblequiz.activity.MainActivity
import com.hobbajt.bubblequiz.levelpacks.di.LevelPacksComponent
import com.hobbajt.bubblequiz.levelpacks.di.LevelPacksModule
import com.hobbajt.bubblequiz.menu.di.MenuComponent
import com.hobbajt.bubblequiz.menu.di.MenuModule
import com.hobbajt.bubblequiz.levels.di.LevelsComponent
import com.hobbajt.bubblequiz.levels.di.LevelsModule
import com.hobbajt.bubblequiz.photo.di.PhotoComponent
import com.hobbajt.bubblequiz.options.di.OptionsComponent
import com.hobbajt.bubblequiz.options.di.OptionsModule
import com.hobbajt.bubblequiz.photo.di.PhotoModule
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent
{
    fun inject(target: MainActivity)

    fun plusMenuComponent(menuModule: MenuModule): MenuComponent
    fun plusOptionsComponent(optionsModule: OptionsModule): OptionsComponent
    fun plusLevelsComponent(levelsModule: LevelsModule): LevelsComponent
    fun plusLevelPacksComponent(levelPacksModule: LevelPacksModule): LevelPacksComponent
    fun plusPhotoComponent(photoModule: PhotoModule): PhotoComponent
}