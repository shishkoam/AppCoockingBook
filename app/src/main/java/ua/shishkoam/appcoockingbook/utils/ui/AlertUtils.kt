package ua.shishkoam.appcoockingbook.utils.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import ua.shishkoam.appcoockingbook.R

object AlertUtils {
    fun showErrorMessage(msg: Int, activity: Context) {
        showErrorMessage(activity.getString(msg), activity)
    }

    fun showErrorMessage(msg: String?, activity: Context?) {
        val adb = AlertDialog.Builder(activity)
        adb.setIcon(R.drawable.ic_error_red_24dp).setTitle(R.string.title_error)
        adb.setMessage(msg).setPositiveButton(R.string.btnContinue, null).create().show()
    }

    private fun start(
        context: Context, icon: Int, title: Int, message: CharSequence,
        positiveButton: Int = -1, positiveListener: DialogInterface.OnClickListener? = null,
        neutralButton: Int = -1, neutralListener: DialogInterface.OnClickListener? = null
    ) {
        val adb = AlertDialog.Builder(context)
        adb.setIcon(icon).setTitle(title)
        if (positiveButton > 0) {
            adb.setPositiveButton(positiveButton, positiveListener)
            adb.setNegativeButton(R.string.cancel, null)
            if (neutralButton > 0) {
                adb.setNeutralButton(neutralButton, neutralListener)
            }
        } else {
            adb.setNeutralButton(R.string.button_ok, positiveListener)
        }
        adb.setMessage(message)
        adb.create().show()
    }
}