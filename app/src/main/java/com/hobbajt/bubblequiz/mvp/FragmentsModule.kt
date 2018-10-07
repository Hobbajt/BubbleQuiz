package com.hobbajt.bubblequiz.mvp

import com.hobbajt.bubblequiz.levelpacks.di.LevelPacksModule
import com.hobbajt.bubblequiz.levelpacks.view.LevelPacksFragment
import com.hobbajt.bubblequiz.levels.di.LevelsModule
import com.hobbajt.bubblequiz.levels.view.LevelsFragment
import com.hobbajt.bubblequiz.menu.di.MenuModule
import com.hobbajt.bubblequiz.menu.view.MenuFragment
import com.hobbajt.bubblequiz.options.di.OptionsModule
import com.hobbajt.bubblequiz.options.view.OptionsFragment
import com.hobbajt.bubblequiz.photo.di.PhotoModule
import com.hobbajt.bubblequiz.photo.view.PhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal abstract class FragmentsModule
{
    @ContributesAndroidInjector(modules = [LevelPacksModule::class])
    abstract fun bindLevelPacksFragment(): LevelPacksFragment

    @ContributesAndroidInjector(modules = [LevelsModule::class])
    abstract fun bindLevelsFragment(): LevelsFragment

    @ContributesAndroidInjector(modules = [MenuModule::class])
    abstract fun bindMenuFragment(): MenuFragment

    @ContributesAndroidInjector(modules = [OptionsModule::class])
    abstract fun bindOptionsFragment(): OptionsFragment

    @ContributesAndroidInjector(modules = [PhotoModule::class])
    abstract fun bindPhotoFragment(): PhotoFragment
}