package ua.shishkoam.appcoockingbook

import android.app.Application
import com.dpforge.primaree.isPrimaryProcess
import com.dpforge.primaree.runIfPrimaryProcess

class App : Application() {


    override fun onCreate() {
        super.onCreate()

        runIfPrimaryProcess {
            // do something in primary process
        }

        if (isPrimaryProcess) {
            // do something in primary process
        } else {
            // do something in secondary process
        }
    }
}