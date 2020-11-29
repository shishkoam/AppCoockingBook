package ua.shishkoam.appcoockingbook.utils.interactors

import android.os.Build
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import ua.shishkoam.appcoockingbook.utils.ui.CoverFragmentDialog
import java.util.*

enum class StartupManager {
    INSTANCE;

    /**EXAMPLE:
    //    StartupManager.INSTANCE.init(this)
    //    StartupManager.INSTANCE.add(
    //    object : Task() {
    //        override fun run(): Boolean {
    //            refreshFragment(null)
    //            return true
    //        }
    //    })  */
    interface Task {
        /**
         * Running body of task
         * @return :
         * - true causes StartupManager to start next task;
         * - false means that this task will invoke StartupManager.INSTANCE.next() by himself to start next task
         */
        fun run(): Boolean
    }

    private var activity: FragmentActivity? = null
    private var covered = false
    private var executing = false
    private val taskQueue: Queue<Task> = LinkedList()
    fun init(activity: FragmentActivity?) {
        this.activity = activity
        covered = false
        executing = false
        taskQueue.clear()
    }

    fun add(task: Task) {
        taskQueue.offer(task)
    }

    fun execute() {
        if (executing) return
        executing = true
        cover()
        next()
    }

    fun finished() {
        next()
    }

    private operator fun next() {
        val task = taskQueue.poll()
        if (task == null) {
            uncover()
            executing = false
            return
        }
        if (task.run()) {
            next()
        }
    }

    private fun cover() {
        if (covered) return
        covered = true
        val actionBar = activity!!.actionBar
        actionBar?.hide()
        val ft = activity!!.supportFragmentManager
        if (ft == null ||
            Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
            && ft.isDestroyed
        ) {
            return
        }
        try {
//            templateDialog.show(getFragmentManager(), "");
            if (coverFragmentDialog != null) {
                try {
                    coverFragmentDialog!!.dismissAllowingStateLoss()
                } catch (ex: NullPointerException) {
                    ex.printStackTrace()
                }
            }
            coverFragmentDialog = CoverFragmentDialog()
            coverFragmentDialog?.show(ft, "coverFragment")
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    private fun uncover() {
        if (!covered) return
        val actionBar = activity!!.actionBar
        actionBar?.show()
        try {
            coverFragmentDialog!!.dismissAllowingStateLoss()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        covered = false
    }

    companion object {
        var coverFragmentDialog: DialogFragment? = null
    }
}