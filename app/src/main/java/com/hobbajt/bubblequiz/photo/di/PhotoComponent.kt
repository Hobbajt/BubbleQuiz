package com.hobbajt.bubblequiz.photo.di

import com.hobbajt.bubblequiz.application.di.scopes.FragmentScope
import com.hobbajt.bubblequiz.photo.view.PhotoFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [PhotoModule::class])
interface PhotoComponent
{
    fun inject(target: PhotoFragment)
}