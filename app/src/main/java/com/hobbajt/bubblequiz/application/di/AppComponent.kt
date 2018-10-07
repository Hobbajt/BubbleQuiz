package com.hobbajt.bubblequiz.application.di

import com.hobbajt.bubblequiz.activity.di.ActivityModule
import com.hobbajt.bubblequiz.application.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class])
interface AppComponent
{
    @Component.Builder
    public interface Builder
    {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}