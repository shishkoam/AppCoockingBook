package ua.shishkoam.appcoockingbook.oldutils.interactors

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Handler
import android.provider.Settings
import ua.shishkoam.appcoockingbook.R

object NetworkProvider {
    private const val AIRPLANE_DIALOG_DELAY_MILLIS = 10000 // 10sec
    fun isNetworkConnected(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
    }

    fun isGpsEnabled(activity: Activity): Boolean {
        val service = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Check airplane mode
     */
    fun checkAirplaneMode(activity: Activity) {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val isAirplaneModeOn = Settings.System.getInt(
                activity.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
            if (!isAirplaneModeOn) {
                if (activity.isFinishing) return@Runnable
                val builder = AlertDialog.Builder(activity)
                builder
                    .setMessage(activity.getString(R.string.airplane_warning))
                    .setCancelable(false)
                    .setPositiveButton(activity.getString(R.string.yes)) { dialog, id ->
                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        activity.startActivity(intent)
                    }
                    .setNeutralButton(activity.getString(R.string.no)) { dialog, id -> dialog.cancel() }
                    .setNegativeButton(activity.getString(R.string.noDoNotAsk)) { dialog, id -> //todo save answer
                        dialog.cancel()
                    }
                val alert = builder.create()
                alert.show()
            }
        }, AIRPLANE_DIALOG_DELAY_MILLIS.toLong())
    }
}