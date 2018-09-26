package com.hobbajt.bubblequiz.levelpacks.view

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.github.lzyzsd.circleprogress.CircleProgress
import com.hobbajt.bubblequiz.R
import com.hobbajt.bubblequiz.utilities.AnimationUtilities
import kotlinx.android.synthetic.main.item_levels_pack.view.*

class LevelPacksAdapter internal constructor(val presenter: LevelPacksPresenter) : RecyclerView.Adapter<LevelPacksAdapter.SetItemHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetItemHolder
    {
        return SetItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_levels_pack, parent, false))
    }

    override fun onBindViewHolder(holder: SetItemHolder, position: Int)
    {
        if (holder.itemViewType == 0)
        {
            presenter.onBindLevelPackViewAtPosition(holder as LevelPackHolderView, position)
        }
    }

    override fun getItemCount(): Int = presenter.getItemsCount()

    inner class SetItemHolder(val view: View) : RecyclerView.ViewHolder(view), LevelPackHolderView
    {
        private val cpSet: CircleProgress = view.cpSet
        private val ivComplete: ImageView = view.ivComplete

        override fun bind()
        {
            cpSet.setOnClickListener {
                presenter.onLevelPackClicked(adapterPosition, this)
            }
        }

        override fun displayDefault()
        {
            itemView.isClickable = true
            AnimationUtilities.animate(cpSet, R.animator.item_in)
        }

        override fun displayComplete()
        {
            cpSet.finishedColor = R.color.green
            ivComplete.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_win))
            cpSet.max = 1
            cpSet.progress = 1
        }

        override fun displayUnlocked(passedLevels: Int, levelsCount: Int)
        {
            cpSet.unfinishedColor = ContextCompat.getColor(view.context, R.color.blue_grey)
            cpSet.finishedColor = ContextCompat.getColor(view.context, R.color.colorPrimary)
            ivComplete.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_lock_open))
            cpSet.max = levelsCount
            cpSet.progress = passedLevels
        }

        override fun displayLocked(requiredLevelsCount: Int, passedLevelsCount: Int)
        {
            cpSet.unfinishedColor = ContextCompat.getColor(view.context, R.color.grey)
            cpSet.finishedColor = ContextCompat.getColor(view.context, R.color.blue_grey)
            ivComplete.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_lock_closed))
            cpSet.max = requiredLevelsCount
            cpSet.progress = passedLevelsCount
        }

        override fun displayLockedAnimation()
        {
            val animation = AnimationUtils.loadAnimation(view.context, R.anim.item_shock)
            cpSet.startAnimation(animation)
            ivComplete.startAnimation(animation)
        }

        override fun displayUnlockedAnimation()
        {
            AnimationUtilities.animate(view, R.animator.item_out)
        }
    }
}
