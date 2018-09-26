package com.hobbajt.bubblequiz.mvp

open class BasePresenter<V>
{
    @Volatile
    protected var view: V? = null

    open fun attachView(view : V)
    {
        this.view = view
    }

    open fun detachView()
    {
        view = null
    }
}
