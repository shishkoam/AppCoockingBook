package ua.shishkoam.appcoockingbook.ui

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    private const val RESPONSE_FORMAT =
        "EEE MMM dd HH:mm:ss ZZZZZ yyyy" // Thu Oct 26 07:31:08 +0000 2017

    private const val MONTH_DAY_FORMAT = "MMM d" // Oct 26

    private fun getFormattedDate(rawDate: String): String? {
        val utcFormat = SimpleDateFormat(RESPONSE_FORMAT, Locale.ROOT)
        val displayedFormat =
            SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault())
        return try {
            val date: Date = utcFormat.parse(rawDate)
            displayedFormat.format(date)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }
}