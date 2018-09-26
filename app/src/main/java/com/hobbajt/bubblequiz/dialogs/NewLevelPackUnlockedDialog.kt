package com.hobbajt.bubblequiz.dialogs

import android.content.Context

import com.afollestad.materialdialogs.MaterialDialog
import com.hobbajt.bubblequiz.R

class NewLevelPackUnlockedDialog(context: Context): BaseDialog()
{
    init
    {
        dialog = MaterialDialog.Builder(context)
                .title(R.string.new_level_pack_unlocked)
                .positiveText(R.string.ok)
                .autoDismiss(true)
                .backgroundColorRes(R.color.blue_grey)
                .titleColorRes(android.R.color.white)
                .positiveColorRes(android.R.color.white)
                .onPositive { dialog, _ -> dialog.dismiss() }
                .cancelable(true)
                .build()
    }
}
