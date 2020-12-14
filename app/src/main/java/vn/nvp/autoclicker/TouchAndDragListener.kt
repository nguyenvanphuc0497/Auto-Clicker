package vn.nvp.autoclicker

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlin.math.pow

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
class TouchAndDragListener(
    private val params: WindowManager.LayoutParams,
    private val startDragDistance: Int = 10,
    private val onTouch: Action?,
    private val onDrag: Action?
) : View.OnTouchListener {
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()
    private var isDrag = false

    private fun isDragging(event: MotionEvent): Boolean =
        (((event.rawX - initialTouchX).toDouble().pow(2.0)
                + (event.rawY - initialTouchY).toDouble().pow(2.0))
                > startDragDistance * startDragDistance)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        event.x.logd("xxx")
        event.y.logd("xxx")
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                isDrag = false
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isDrag && isDragging(event)) {
                    isDrag = true
                }
                if (!isDrag) return true
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                onDrag?.invoke()
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (!isDrag) {
                    onTouch?.invoke()
                    return true
                }
            }
        }
        return false
    }
}
