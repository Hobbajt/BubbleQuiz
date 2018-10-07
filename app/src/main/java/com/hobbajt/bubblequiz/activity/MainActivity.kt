package com.hobbajt.bubblequiz.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.hobbajt.bubblequiz.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity: AppCompatActivity(), HasSupportFragmentInjector
{
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var fragmentsManager: FragmentsManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onResume()
    {
        super.onResume()
        fragmentsManager.onResume()
    }

    override fun onBackPressed()
    {
        if(!fragmentsManager.onBackPressed())
        {
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
}
