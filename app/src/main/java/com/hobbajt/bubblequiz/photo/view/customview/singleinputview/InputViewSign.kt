package com.hobbajt.bubblequiz.photo.view.customview.singleinputview

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnectionWrapper

class InputViewSign : AppCompatEditText, View.OnKeyListener, View.OnClickListener
{
    private var listener: SignBoxChangeListener? = null
    internal val textWatcher: TextWatcher = object : TextWatcher
    {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
        {
            if (!isEmpty())
            {
                listener?.moveToNextBox(id)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    internal interface SignBoxChangeListener
    {
        fun moveToPreviousBox(currentId: Int)

        fun moveToNextBox(currentId: Int)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { createListeners() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { createListeners() }

    constructor(context: Context) : super(context) { createListeners() }



    // region Listeners
    private fun createListeners()
    {
        addTextChangedListener(textWatcher)
        setOnKeyListener(this)
        setOnClickListener(this)
    }

    internal fun setOnSignBoxChangeListener(listener: SignBoxChangeListener)
    {
        this.listener = listener
    }

    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean
    {
        if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL)
        {
            onDelete()
        }
        return false
    }

    override fun onClick(view: View)
    {
        // Empty for handle clicks
    }
    // endregion

    private fun onDelete()
    {
        if (isEmpty())
        {
            listener?.moveToPreviousBox(id)
        }
        else
        {
            removeLetter()
        }
    }

    fun removeLetter()
    {
        setText("")
    }

    private fun isEmpty() = text.toString().trim().isEmpty()


    fun setColor(color: Int)
    {
        setTextColor(color)
        isEnabled = false
    }

    // region Input Connection
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection
    {
        return InputConnection(super.onCreateInputConnection(outAttrs), true)
    }

    inner class InputConnection internal constructor(target: android.view.inputmethod.InputConnection, mutable: Boolean) : InputConnectionWrapper(target, mutable)
    {
        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean
        {
            return if (beforeLength == 1 && afterLength == 0)
            {
                (sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL)))
            }
            else
            {
                super.deleteSurroundingText(beforeLength, afterLength)
            }
        }
    }
    // endregion
}