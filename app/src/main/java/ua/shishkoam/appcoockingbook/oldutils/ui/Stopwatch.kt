package ua.shishkoam.appcoockingbook.oldutils.ui

enum class Stopwatch {
    INSTANCE;

    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    var isRunning = false
        private set

    fun start() {
        isRunning = true
        startTime = System.currentTimeMillis()
    }

    fun stop() {
        isRunning = false
        elapsedTime = time
    }

    fun reset() {
        isRunning = false
        elapsedTime = 0
    }

    val time: Long
        get() = System.currentTimeMillis() - startTime + elapsedTime

    override fun toString(): String {
        val time = time
        val miliseconds = time % 1000 / 100
        val milisecondsStr = miliseconds.toString()
        val seconds = time / 1000 % 60
        val secondsStr = if (seconds < 10) "0$seconds" else seconds.toString()
        val minutes = time / 60000 % 60
        val minutesStr = if (minutes < 10) "0$minutes" else minutes.toString()
        if (time > 3600000) {
            val hoursStr = (time / 3600000 % 60).toString()
            return "$hoursStr:$minutesStr:$secondsStr:$milisecondsStr"
        }
        return "$minutesStr:$secondsStr:$milisecondsStr"
    }
}