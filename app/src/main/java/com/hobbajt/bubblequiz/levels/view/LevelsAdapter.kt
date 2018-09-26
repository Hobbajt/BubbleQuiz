package com.hobbajt.bubblequiz.levels.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.hobbajt.bubblequiz.R
import kotlinx.android.synthetic.main.item_level.view.*

class LevelsAdapter(private val presenter : LevelsPresenter) : RecyclerView.Adapter<LevelsAdapter.LevelItemHolder>()
{
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) = LevelItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false))

    override fun onBindViewHolder(holder : LevelItemHolder, position : Int)
    {
        presenter.onBindLevelViewAtPosition(holder, position)
    }

    override fun getItemCount() = presenter.getItemCount()

    inner class LevelItemHolder(val view : View) : RecyclerView.ViewHolder(view), LevelHolderView
    {
        private val tvNumber : TextView = view.tvPart

        override fun bind(position: Int, isLevelPassed: Boolean, presenter: LevelsPresenter)
        {
            tvNumber.text = "${position + 1}"
            tvNumber.setBackgroundResource(getIconRes(isLevelPassed))
            tvNumber.startAnimation(AnimationUtils.loadAnimation(tvNumber.context, R.anim.item_menu_show_animation))
            tvNumber.setOnClickListener {
                presenter.onLevelClicked(position, this)
            }
        }

        override fun displayOpenAnimation()
        {
            AnimationUtils.loadAnimation(view.context, R.anim.item_menu_select_animation)
        }

        private fun getIconRes(isLevelPassed : Boolean) = if(isLevelPassed) R.drawable.passed_circle_level_item else R.drawable.not_passed_circle_level_item
    }
}