package com.hobbajt.bubblequiz.menu.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.levelpacks.view.LevelPacksFragment
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import com.hobbajt.bubblequiz.options.view.OptionsFragment
import com.hobbajt.bubblequiz.utilities.AnimationUtilities
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

class MenuFragment: BaseMVPFragment<MenuPresenter>(), MenuContract.View
{
    @Inject
    lateinit var presenter: MenuPresenter

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun displayUI()
    {
        (ivPlay as ImageView).setImageResource(R.drawable.ic_play)
        (ivOptions as ImageView).setImageResource(R.drawable.ic_options)

        playContainer.setOnClickListener{ presenter.onPlayClicked()}
        optionsContainer.setOnClickListener{ presenter.onOptionsClicked()}
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onResume()
    {
        super.onResume()
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.item_slide_up)
        playContainer.startAnimation(slideUp)
        optionsContainer.startAnimation(slideUp)
    }

    override fun openOptions()
    {
        AnimationUtilities.animate(ivOptions, R.animator.item_out)
        fragmentsManager.changeFragment(OptionsFragment.newInstance(), true)
    }

    override fun openLevelPacks()
    {
        AnimationUtilities.animate(ivPlay, R.animator.item_out)
        fragmentsManager.changeFragment(LevelPacksFragment.newInstance(), true)
    }

    companion object
    {
        fun newInstance() = MenuFragment()
    }
}