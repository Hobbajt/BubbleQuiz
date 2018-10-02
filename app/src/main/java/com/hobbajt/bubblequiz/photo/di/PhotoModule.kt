package com.hobbajt.bubblequiz.photo.di

import com.hobbajt.bubblequiz.application.Api
import com.hobbajt.bubblequiz.photo.model.PhotoApiLoader
import com.hobbajt.bubblequiz.photo.view.PhotoPresenter
import com.hobbajt.bubblequiz.sharedprefs.LocalDataEditor
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named

@Module
class PhotoModule
{
    @Provides
    @Named("LANGUAGE")
    fun providesLanguage() = Locale.getDefault().language.toUpperCase()


    @Provides
    fun providesPhotoApiLoader(api: Api): PhotoApiLoader = PhotoApiLoader(api)

    @Provides
    fun providesPhotoPresenter(localDataEditor: LocalDataEditor,
                               photoApiLoader: PhotoApiLoader,
                               @Named("LANGUAGE") language: String,
                               @Named("SCREEN_SIZE") screenSize: Int): PhotoPresenter =
            PhotoPresenter(localDataEditor, photoApiLoader, language, screenSize)

    @Provides
    fun providesDisplayImageOptions() = DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.EXACTLY)
            .build()


}