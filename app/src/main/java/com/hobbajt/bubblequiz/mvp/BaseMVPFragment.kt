package com.hobbajt.bubblequiz.mvp

import android.content.Context
import com.hobbajt.bubblequiz.activity.FragmentsManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseMVPFragment<T : BasePresenter<*>>: DaggerFragment()
{
    @Inject
    lateinit var fragmentsManager: FragmentsManager

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        attachView()
    }

    abstract fun attachView()

    abstract fun providePresenter(): T

    override fun onDetach()
    {
        super.onDetach()
        providePresenter().detachView()
    }
}