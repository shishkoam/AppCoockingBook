package ua.shishkoam.appcoockingbook.oldutils.ui

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import ua.shishkoam.appcoockingbook.R

class StopWatchView : RelativeLayout, View.OnClickListener {
    var timeTextView: TextView? = null
    var startButton: ImageButton? = null
    var resetButton: ImageButton? = null
    var closeButton: ImageButton? = null
    var tHandler: Handler? = null

    var runStopwatch: Runnable = object : Runnable {
        override fun run() {
            tHandler?.postDelayed(this, 100)
            timeTextView?.text = Stopwatch.INSTANCE.toString()
        }
    }

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.stop_watch_view, this, true)
        startButton = view.findViewById<View>(R.id.startButton) as ImageButton
        resetButton = view.findViewById<View>(R.id.resetButton) as ImageButton
        timeTextView = view.findViewById<View>(R.id.timeTextView) as TextView
        closeButton = view.findViewById<View>(R.id.closeButton) as ImageButton
        startButton?.setOnClickListener(this)
        resetButton?.setOnClickListener(this)
        closeButton?.setOnClickListener(this)
        tHandler = Handler()
        if (Stopwatch.INSTANCE.isRunning) {
            tHandler?.post(runStopwatch)
            startButton?.setImageResource(R.drawable.pause)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.startButton -> if (Stopwatch.INSTANCE.isRunning) {
                Stopwatch.INSTANCE.stop()
                tHandler?.removeCallbacks(runStopwatch)
                startButton?.setImageResource(R.drawable.start)
            } else {
                Stopwatch.INSTANCE.start()
                tHandler?.post(runStopwatch)
                startButton?.setImageResource(R.drawable.pause)
            }
            R.id.resetButton -> {
                Stopwatch.INSTANCE.reset()
                tHandler?.removeCallbacks(runStopwatch)
                timeTextView?.text = "00:00:0"
                startButton?.setImageResource(R.drawable.start)
            }
            R.id.closeButton -> {
                Stopwatch.INSTANCE.reset()
                tHandler?.removeCallbacks(runStopwatch)
                timeTextView?.text = "00:00:0"
                startButton?.setImageResource(R.drawable.start)
                this.visibility = GONE
            }
        }
    }
}