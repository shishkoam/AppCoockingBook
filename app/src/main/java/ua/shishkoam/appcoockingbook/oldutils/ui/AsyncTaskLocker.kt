package ua.shishkoam.appcoockingbook.oldutils.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import ua.shishkoam.appcoockingbook.R

class AsyncTaskLocker(context: Context) : AsyncTask<Void?, Void?, Void?>() {
    private val activity: Activity
    private val dialog: Dialog?
    private val textViewMessage: TextView
    private val progressCircle: ProgressBar
    private val progressBar: ProgressBar
    private val button: Button
    private var worker: LockerWorker? = null
    private var isSticky = false
    private var hardCancel = false
    private var cancelled = false
    fun setTitle(title: String?): AsyncTaskLocker {
        activity.runOnUiThread { dialog!!.setTitle(title) }
        return this
    }

    fun setMessage(message: CharSequence?): AsyncTaskLocker {
        activity.runOnUiThread { textViewMessage.text = message }
        return this
    }

    fun setAlpha(alpha: Int): AsyncTaskLocker {
        activity.runOnUiThread { alpha(alpha) }
        return this
    }

    private fun alpha(alpha: Int) {
        val back = dialog!!.window!!.decorView.background
        if (back != null) {
            back.alpha = alpha
        }
    }

    /** Set visibility of "Cancel" button and possibility of cancel background operation.
     * Since user click "Cancel" method isCanceled() will return true. isCanceled method is
     * method of AsyncTaskLockerDialog which will be passed as argument of backgroundAction
     * method of LockerWorker interface
     *
     * @param isCancelable if true causes dialog show "Cancel" button. Default value is true.
     * @param hardCancel if true then locker try to cancel async task and hide dialog
     * @return this object to further using setters or execute
     */
    fun setCancellable(isCancelable: Boolean, hardCancel: Boolean): AsyncTaskLocker {
        this.hardCancel = hardCancel
        activity.runOnUiThread { button.visibility = if (isCancelable) View.VISIBLE else View.GONE }
        return this
    }

    /** Set type of progressbar.
     *
     * @param isProgress true if progressbar have to be horizontal or false (default) if progressbar
     * is spinner
     * @return this object to further using setters or execute
     */
    fun setIsProgress(isProgress: Boolean): AsyncTaskLocker {
        activity.runOnUiThread {
            progressCircle.visibility =
                if (isProgress) View.GONE else View.VISIBLE
            progressBar.visibility = if (isProgress) View.VISIBLE else View.GONE
        }
        return this
    }

    /** Set behavior of dialog after finish of backgroundAction.
     *
     * @param isSticky if true then dialog will stay visible and change button text from "Cancel" to "OK".
     * In this case dialog will be closed (and also postAction() will be started) when user click "OK" or
     * when will be invoked close() of AsyncTaskLockerDialog which passed as argument of backgroundAction
     * method of LockerWorker interface
     * If false (default) dialog will be closed immediately after finish of backgroundAction().
     * @return this object to further using setters or execute
     */
    fun setIsSticky(isSticky: Boolean): AsyncTaskLocker {
        this.isSticky = isSticky
        return this
    }

    fun setMax(max: Int) {
        activity.runOnUiThread { progressBar.max = max }
    }

    var progress: Int
        get() = progressBar.progress
        set(progress) {
            activity.runOnUiThread { progressBar.progress = progress }
        }

    fun cancelled(): Boolean {
        return cancelled
    }

    fun setWorker(worker: LockerWorker?): AsyncTaskLocker {
        this.worker = worker
        return this
    }

    override fun onPreExecute() {
        super.onPreExecute()
        dialog!!.show()
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        if (isSticky) {
            button.visibility = View.VISIBLE
            button.setText(R.string.button_ok)
            button.isEnabled = true
            button.setOnClickListener { runPostAction() }
            dialog!!.setCancelable(true)
        } else {
            runPostAction()
        }
    }

    private fun runPostAction() {
        if (dialog != null && dialog.isShowing) dialog.dismiss()
        if (worker != null) worker!!.postAction()
    }

    interface LockerWorker {
        fun backgroundAction()
        fun postAction()
    }

    companion object {
        private const val defaultAlpha = 216 // Default alpha value of dialog background
    }

    init {
        activity = context as Activity
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_progresslocker)
        alpha(defaultAlpha)
        textViewMessage = dialog.findViewById<View>(R.id.lockerMessage) as TextView
        progressCircle = dialog.findViewById<View>(R.id.progressCircle) as ProgressBar
        progressBar = dialog.findViewById<View>(R.id.horizontalProgressBar) as ProgressBar
        button = dialog.findViewById<View>(R.id.button) as Button
        button.setOnClickListener { view ->
            cancelled = true
            view.isEnabled = false
            if (hardCancel) {
                cancel(true)
                dialog.dismiss()
            }
        }
        dialog.setCancelable(false)
    }

    override fun doInBackground(vararg params: Void?): Void? {
        if (worker != null) worker!!.backgroundAction()
        return null
    }
}