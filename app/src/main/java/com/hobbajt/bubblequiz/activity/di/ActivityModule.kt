package com.hobbajt.bubblequiz.activity.di

import com.hobbajt.bubblequiz.activity.MainActivity
import com.hobbajt.bubblequiz.mvp.FragmentsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule
{
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}