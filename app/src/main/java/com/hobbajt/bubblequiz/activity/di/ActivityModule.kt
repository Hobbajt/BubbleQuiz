package com.hobbajt.bubblequiz.activity.di

import android.support.v4.app.FragmentActivity
import com.hobbajt.bubblequiz.activity.FragmentsManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val fragmentActivity: FragmentActivity)
{
    @Provides
    fun providesFragmentsManager(): FragmentsManager = FragmentsManager(fragmentActivity.supportFragmentManager)

}