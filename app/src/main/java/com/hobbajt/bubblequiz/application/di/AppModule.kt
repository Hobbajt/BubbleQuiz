package com.hobbajt.bubblequiz.application.di

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.view.WindowManager
import com.google.gson.Gson
import com.hobbajt.bubblequiz.BuildConfig
import com.hobbajt.bubblequiz.application.App
import com.hobbajt.bubblequiz.application.Api
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val app: App)
{
    @Singleton
    @Provides
    fun providesApplication() = app


    @Provides
    @Named("SCREEN_SIZE")
    fun providesScreenWidth(app: App): Int
    {
        val windowManager = app.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    @Singleton
    @Provides
    fun providesImageLoaderConfiguration(app: App, @Named("SCREEN_SIZE") screenWidth: Int) = ImageLoaderConfiguration.Builder(app.applicationContext)
            .diskCacheExtraOptions(screenWidth, screenWidth, null)
            .threadPriority(Thread.MAX_PRIORITY)
            .denyCacheImageMultipleSizesInMemory()
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
            .writeDebugLogs()
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesRequestInterface(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun providesGson(): Gson = Gson()

    @Singleton
    @Provides
    fun providesSharedPreferences(app: App): SharedPreferences = app.getSharedPreferences("applicationPrefs", 0)

    @Singleton
    @Provides
    fun providesLocalDataEditor(sharedPreferences: SharedPreferences, gson: Gson): LocalDataEditor = LocalDataEditor(sharedPreferences, gson)

}