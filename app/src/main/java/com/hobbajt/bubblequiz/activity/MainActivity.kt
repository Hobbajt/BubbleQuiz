package com.hobbajt.bubblequiz.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.activity.di.ActivityComponent
import com.hobbajt.bubblequiz.activity.di.ActivityModule
import com.hobbajt.bubblequiz.application.App
import javax.inject.Inject

class MainActivity: AppCompatActivity()
{
    @Inject
    lateinit var fragmentsManager: FragmentsManager

    var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        initDependencies()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onResume()
    {
        super.onResume()
        fragmentsManager.onResume()
    }

    private fun initDependencies()
    {
        if(activityComponent == null)
        {
            activityComponent = (application as App).appComponent.plus(ActivityModule(this))
        }
        activityComponent?.inject(this)
    }

    override fun onBackPressed()
    {
        if(!fragmentsManager.onBackPressed())
        {
            super.onBackPressed()
        }
    }
}
