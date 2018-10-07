package com.hobbajt.bubblequiz.mvp

open class BasePresenter<V>
{
    @Volatile
    var view: V? = null

    open fun attachView(view : V)
    {
        this.view = view
    }

    open fun detachView()
    {
        view = null
    }
}
