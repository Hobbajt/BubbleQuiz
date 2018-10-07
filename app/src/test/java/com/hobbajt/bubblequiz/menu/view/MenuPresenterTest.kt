package com.hobbajt.bubblequiz.menu.view

import org.junit.Before

class MenuPresenterTest
{
    lateinit var presenter: MenuPresenter
    lateinit var view: MenuContract.View

    @Before
    fun setUp()
    {
        presenter = MenuPresenter()
    }
}