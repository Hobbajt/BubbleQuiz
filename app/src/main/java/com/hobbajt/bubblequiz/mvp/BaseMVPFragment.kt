package com.hobbajt.bubblequiz.mvp

import android.content.Context
import android.support.v4.app.Fragment
import com.hobbajt.bubblequiz.activity.FragmentsManager
import javax.inject.Inject

abstract class BaseMVPFragment<T : BasePresenter<*>>: Fragment()
{
    @Inject
    lateinit var fragmentsManager: FragmentsManager

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        initDependencies()
        attachView()
    }

    abstract fun attachView()

    abstract fun providePresenter(): T

    protected abstract fun initDependencies()

    override fun onDetach()
    {
        super.onDetach()
        providePresenter().detachView()
    }
}