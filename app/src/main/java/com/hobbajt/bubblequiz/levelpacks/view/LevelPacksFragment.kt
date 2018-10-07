package com.hobbajt.bubblequiz.levelpacks.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.dialogs.ConnectionErrorDialog
import com.hobbajt.bubblequiz.dialogs.OnDialogClickListener
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.levels.view.LevelsFragment
import com.hobbajt.bubblequiz.mvp.BaseMVPFragment
import kotlinx.android.synthetic.main.fragment_level.*
import java.io.Serializable
import javax.inject.Inject

class LevelPacksFragment : BaseMVPFragment<LevelPacksPresenter>(), LevelPacksContract.View
{
    @Inject
    lateinit var presenter: LevelPacksPresenter

    override fun attachView()
    {
        presenter.attachView(this)
    }

    override fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_level, container, false)
        container?.clipChildren = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val levelsPacks = savedInstanceState?.getSerializable(LEVELS_PACKS_TAG) as List<LevelsPack>?
        presenter.onViewCreated(levelsPacks)
    }

    override fun displayData()
    {
        val gridLayoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        rvItems.clipChildren = false
        rvItems.layoutManager = gridLayoutManager
        rvItems.adapter = LevelPacksAdapter(presenter)
    }

    override fun displayDownloadErrorDialog()
    {
        context?.let {
            ConnectionErrorDialog(it, object: OnDialogClickListener {
                override fun onPositiveButtonClicked()
                {
                    presenter.onRetryButtonClicked()
                }

                override fun onNegativeButtonClicked()
                {
                    presenter.onCancelButtonClicked()
                }

            }).show()
        }
    }

    override fun openLevels(levelPack: LevelsPack)
    {
        val levelsFragment = LevelsFragment.newInstance(levelPack)
        fragmentsManager.changeFragment(levelsFragment, true)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        val levelsPacks = presenter.onSaveState()
        outState.putSerializable(LEVELS_PACKS_TAG, levelsPacks as Serializable)
    }

    companion object
    {
        const val LEVELS_PACKS_TAG = "levelsPacks"
        fun newInstance() = LevelPacksFragment()
    }
}
