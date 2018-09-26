package com.hobbajt.bubblequiz.utilities

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView


object Utilities
{
    fun animateContent(ivAnimationLayer : ImageView, from : Float, to : Float)
    {
        ivAnimationLayer.visibility = View.VISIBLE
        val animation = AlphaAnimation(from, to)
        animation.duration = 1000
        animation.repeatCount = 0

        animation.setAnimationListener(object : Animation.AnimationListener
        {
            override fun onAnimationStart(animation : Animation) {}

            override fun onAnimationRepeat(animation : Animation) {}

            override fun onAnimationEnd(animation : Animation)
            {
                ivAnimationLayer.visibility = View.GONE
            }
        })

        ivAnimationLayer.startAnimation(animation)
    }
}