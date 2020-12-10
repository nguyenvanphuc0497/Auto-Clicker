package vn.nvp.autoclicker.bean

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Point
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
abstract class Event {
    var startTime = 10L
    var duration = 10L
    lateinit var path: Path

    @RequiresApi(Build.VERSION_CODES.N)
    fun onEvent(): GestureDescription.StrokeDescription {
        path = Path()
        movePath()
        return GestureDescription.StrokeDescription(path, startTime, duration)
    }

    abstract fun movePath()
}

data class Move(val to: Point) : Event() {
    override fun movePath() {
        path.moveTo(to.x.toFloat(), to.y.toFloat())
    }
}

data class Click(val to: Point) : Event() {
    override fun movePath() {
        path.moveTo(to.x.toFloat(), to.y.toFloat())
    }
}

data class Swipe(val from: Point, val to: Point) : Event() {
    override fun movePath() {
        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
    }
}
