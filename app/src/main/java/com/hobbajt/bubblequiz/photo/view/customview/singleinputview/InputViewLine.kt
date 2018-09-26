package com.hobbajt.bubblequiz.photo.view.customview.singleinputview

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.hobbajt.bubblequiz.R
import org.apache.commons.lang3.StringUtils


class InputViewLine : LinearLayout, InputViewSign.SignBoxChangeListener
{
    private var signsCount: Int = 0
    private var etSigns: Array<InputViewSign> = emptyArray()
    private var onLineChangedListener: OnLineChangedListener? = null

    val signs: String
        get()
        {
            val builder = StringBuilder()
            for (etSign in etSigns)
                builder.append(getSign(etSign))
            return builder.toString()
        }

    interface OnLineChangedListener
    {
        fun onLineFilled(lineId: Int, signs: String)
        fun focusNextLine(lineId: Int)
        fun focusPreviousLine(lineId: Int)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun addSignBoxes()
    {
        val inputFilters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(1))
        etSigns = Array(signsCount) { createSignBox(it, inputFilters) }
    }

    private fun createSignBox(i: Int, inputFilters: Array<InputFilter>): InputViewSign
    {
        val view = LayoutInflater.from(context).inflate(R.layout.sign_input_view, this)
        val etSign = view.findViewById<InputViewSign>(R.id.etSignBox)
        etSign.filters = inputFilters
        etSign.id = i
        etSign.imeOptions = EditorInfo.IME_ACTION_NEXT
        etSign.tag = i
        etSign.setOnSignBoxChangeListener(this)
        return etSign
    }

    private fun isLineFilled(): Boolean
    {
        for (etSign in etSigns)
        {
            if (StringUtils.isBlank(getSign(etSign)))
                return false
        }
        return true
    }

    override fun moveToPreviousBox(currentID: Int)
    {
        if (currentID > 0)
            moveToBox(currentID - 1)
        else
            onLineChangedListener?.focusPreviousLine(id)
    }

    override fun moveToNextBox(currentId: Int)
    {
        if (isLineFilled())
            onLineChangedListener?.onLineFilled(tag as Int, signs)

        if (currentId + 1 < etSigns.size)
            etSigns[currentId + 1].requestFocus()
        else
            onLineChangedListener?.focusNextLine(id)
    }

    fun moveToBox(id: Int)
    {
        etSigns[id].requestFocus()
        etSigns[id].removeLetter()
    }

    fun displayWord(text: String)
    {
        for (i in etSigns.indices)
        {
            if (etSigns[i].handler != null)
            {
                etSigns[i].removeTextChangedListener(etSigns[i].textWatcher)
                etSigns[i].setText("${text[i]}")
            }
        }
    }

    fun setOnLineChangedListener(onLineChangedListener: OnLineChangedListener)
    {
        this.onLineChangedListener = onLineChangedListener
    }

    fun setSignsCount(signsCount: Int)
    {
        this.signsCount = signsCount
        if (signsCount > 0)
        {
            addSignBoxes()
            etSigns[0].requestFocus()
            invalidate()
            requestLayout()
        }
    }

    private fun getSign(etSign: InputViewSign): String
    {
        return etSign.text.toString()
    }

    override fun setEnabled(isEnabled: Boolean)
    {
        for (etSign in etSigns)
            etSign.isEnabled = isEnabled
    }

    fun setColor(color: Int)
    {
        for (etSign in etSigns)
            etSign.setColor(color)
    }
}