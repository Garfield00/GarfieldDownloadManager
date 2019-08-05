package com.garfield.download.utils

import android.app.Dialog
import android.content.Context
import com.garfield.download.R
import com.garfield.download.dialog.TipDialog
import com.garfield.download.dialog.builder.TipDialogBuilder

/**
 * Author chunliangwang
 * Date 2019-06-27
 * Description 弹窗工具方法
 */

fun showTipDialog(context: Context, title: String, listener: TipDialog.TipDialogListener): Dialog {
    val dialog = TipDialogBuilder.newInstance(context).setTitle(title).setContent("")
        .setSureText(context.getString(R.string.tip_sure))
        .setCancelText(context.getString(R.string.tip_cancel))
        .setListener(listener).create()
    dialog.show()
    return dialog
}
