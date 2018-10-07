package com.hobbajt.bubblequiz.application

import android.app.Activity
import android.app.Application
import com.hobbajt.bubblequiz.application.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector
{
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    private lateinit var instance: App

    override fun onCreate()
    {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        instance = this
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}