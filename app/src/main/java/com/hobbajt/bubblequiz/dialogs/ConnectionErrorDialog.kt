package com.hobbajt.bubblequiz.dialogs

import android.content.Context

import com.afollestad.materialdialogs.MaterialDialog
import com.hobbajt.bubblequiz.R


class ConnectionErrorDialog(context: Context, listener: OnDialogClickListener): BaseDialog()
{
    init
    {
        dialog = MaterialDialog.Builder(context)
                .title(R.string.connection)
                .positiveText(R.string.try_again)
                .negativeText(R.string.exit)
                .autoDismiss(false)
                .iconRes(android.R.drawable.ic_dialog_alert)
                .onPositive { dialog, _ ->
                    listener.onPositiveButtonClicked()
                    dialog.dismiss()
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                    listener.onNegativeButtonClicked() }
                .cancelable(false)
                .build()
    }
}
