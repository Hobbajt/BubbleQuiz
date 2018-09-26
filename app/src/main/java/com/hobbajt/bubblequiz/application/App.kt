package com.hobbajt.bubblequiz.application

import android.app.Application
import com.hobbajt.bubblequiz.application.di.AppComponent
import com.hobbajt.bubblequiz.application.di.AppModule
import com.hobbajt.bubblequiz.application.di.DaggerAppComponent

class App : Application()
{
    private lateinit var instance: App
    lateinit var appComponent: AppComponent

    override fun onCreate()
    {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        instance = this
    }
}