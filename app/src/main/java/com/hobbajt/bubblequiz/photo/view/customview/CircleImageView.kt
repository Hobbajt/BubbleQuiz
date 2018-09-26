package com.hobbajt.bubblequiz.photo.view.customview

import android.content.Context
import android.util.AttributeSet

class CircleImageView : android.support.v7.widget.AppCompatImageView
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
