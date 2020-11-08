package ua.shishkoam.appcoockingbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ua.shishkoam.appcoockingbook.ui.features.RecyclerViewFragment
import ua.shishkoam.appcoockingbook.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, RecyclerViewFragment.newInstance(1))
                    .commitNow()
        }
    }
}