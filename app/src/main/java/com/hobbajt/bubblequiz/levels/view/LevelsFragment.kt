package com.hobbajt.bubblequiz.levels.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import com.hobbajt.bubblequiz.photo.view.PhotoFragment
import kotlinx.android.synthetic.main.fragment_level.*
import javax.inject.Inject

class LevelsFragment: BaseMVPFragment<LevelsPresenter>(), LevelsContract.View
{
    @Inject
    lateinit var presenter: LevelsPresenter

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val levelsPackInfo = arguments?.getSerializable(LEVELS_PACK_INFO_TAG) as LevelsPack
        presenter.onCreateView(levelsPackInfo)
    }

    override fun displayData()
    {
        val gridLayoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        val adapter = LevelsAdapter(presenter)
        rvItems.clipChildren = false
        rvItems.layoutManager = gridLayoutManager
        rvItems.adapter = adapter
    }

    override fun openPhoto(levelsPack: LevelsPack, position: Int)
    {
        val fragment = PhotoFragment.newInstance(levelsPack, position)
        fragmentsManager.changeFragment(fragment, true)
    }

    companion object
    {
        private const val LEVELS_PACK_INFO_TAG = "levelsPackInfo"

        fun newInstance(levelPack: LevelsPack): LevelsFragment
        {
            val bundle = Bundle()
            bundle.putSerializable(LEVELS_PACK_INFO_TAG, levelPack)
            val fragment = LevelsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}