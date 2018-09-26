package com.hobbajt.bubblequiz.application.di

import com.hobbajt.bubblequiz.activity.di.ActivityComponent
import com.hobbajt.bubblequiz.activity.di.ActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent
{
    fun plus(activityModule: ActivityModule): ActivityComponent
}