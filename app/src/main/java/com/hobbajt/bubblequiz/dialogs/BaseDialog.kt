package com.hobbajt.bubblequiz.dialogs

import com.afollestad.materialdialogs.MaterialDialog

open class BaseDialog
{
    lateinit var dialog: MaterialDialog

    fun show()
    {
        if(!dialog.isShowing)
        {
            dialog.show()
        }
    }
}