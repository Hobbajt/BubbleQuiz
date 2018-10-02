package com.hobbajt.bubblequiz.options.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.activity.MainActivity
import com.hobbajt.bubblequiz.options.di.OptionsComponent
import com.hobbajt.bubblequiz.options.di.OptionsModule
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import kotlinx.android.synthetic.main.fragment_options.*
import javax.inject.Inject

class OptionsFragment : BaseMVPFragment<OptionsPresenter>(), OptionsContract.View
{
    @Inject
    lateinit var presenter: OptionsPresenter
    private var optionsComponent: OptionsComponent? = null

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?
    {
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun initDependencies()
    {
        if(optionsComponent == null)
        {
            context?.let {
                optionsComponent = (activity as MainActivity).activityComponent?.plusOptionsComponent(OptionsModule())
                optionsComponent?.inject(this)
            }
        }
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
        clRemoveData.startAnimation(slideUp)
    }

    override fun displayUI()
    {
        (ivRemoveData as ImageView).setImageResource(R.drawable.ic_delete)
        clRemoveData.setOnClickListener { presenter.onRemoveDataClicked() }
    }

    override fun displayUserDataRemoved()
    {
        view?.let {
            Snackbar.make(it, R.string.remove_user_data_finished, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object
    {
        fun newInstance() = OptionsFragment()
    }
}
