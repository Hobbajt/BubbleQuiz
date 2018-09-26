package com.hobbajt.bubblequiz.utilities

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.hobbajt.bubblequiz.R

object AnimationUtilities
{
    fun animate(view : View, animationID : Int)
    {
        val animatorSet = AnimatorInflater.loadAnimator(view.context, animationID) as AnimatorSet
        animatorSet.setTarget(view)
        animatorSet.start()
    }

    fun animatePhotoIn(photo : View, bubbles : View?)
    {
        photo.visibility = View.VISIBLE
        bubbles?.let { animate(it, R.animator.photo_out) }
        animate(photo, R.animator.item_fade_in)
        bubbles?.let { bubbles.visibility = View.GONE }
    }
}
