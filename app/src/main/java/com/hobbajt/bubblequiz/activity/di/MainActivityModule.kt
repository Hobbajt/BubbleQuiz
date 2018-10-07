package com.hobbajt.bubblequiz.activity.di

import com.hobbajt.bubblequiz.activity.FragmentsManager
import com.hobbajt.bubblequiz.activity.MainActivity
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule
{
    @Provides
    fun providesFragmentsManager(activity: MainActivity): FragmentsManager = FragmentsManager(activity.supportFragmentManager)
}