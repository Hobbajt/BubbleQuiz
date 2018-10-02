package com.hobbajt.bubblequiz.photo.view.customview

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        AppCompatImageView(context, attrs, defStyleAttr)
{
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
