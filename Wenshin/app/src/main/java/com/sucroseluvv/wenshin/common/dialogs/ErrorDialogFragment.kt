package com.sucroseluvv.wenshin.common.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sucroseluvv.wenshin.App

class ErrorDialogFragment(message: String, title: String = "Ошибка") : DialogFragment() {
    val message = message
    val title = title
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Ок", { dialog, id -> })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}