package com.hobbajt.bubblequiz.levels.view.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class LevelTileLayout : RelativeLayout
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}