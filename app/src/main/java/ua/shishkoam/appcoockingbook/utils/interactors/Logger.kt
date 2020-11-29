package ua.shishkoam.appcoockingbook.utils.interactors

import ua.shishkoam.appcoockingbook.utils.interactors.storage.StringFileStorage
import ua.shishkoam.appcoockingbook.utils.interactors.storage.StringStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    fun appendLog(text: String?) {
        //remove it if need logging
        if (true) return
        val logFile = File("filepath", "mapLog.txt")
        val storage: StringStorage = StringFileStorage()
        storage.save(text!!, logFile.absolutePath)
    }

    fun appendLogWithTime(text: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK)
        appendLog(sdf.format(Date(System.currentTimeMillis())) + " " + text)
    }

    fun appendLogWithTime(text: String, list: ArrayList<String?>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK)
        val sb = StringBuilder()
        for (i in list.indices) {
            sb.append(list[i]).append(";")
        }
        appendLog(sdf.format(Date(System.currentTimeMillis())) + " " + text + sb.toString())
    }

    fun writeException(e: Exception) {
        val sb = getString(e) ?: return
        appendLogWithTime(sb.toString())
    }

    fun getString(e: Exception): StringBuilder? {
        e.message?.let {
            val sb = StringBuilder(it)
            val stack = e.stackTrace
            for (aStack in stack) {
                sb.append(aStack.toString())
                sb.append("\n")
            }
            return sb
        }
        return null
    }
}