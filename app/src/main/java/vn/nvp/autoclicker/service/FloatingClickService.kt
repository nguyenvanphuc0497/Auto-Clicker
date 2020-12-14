package vn.nvp.autoclicker.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import vn.nvp.autoclicker.TouchAndDragListener
import vn.nvp.autoclicker.dp2px
import vn.nvp.autoclicker.logd
import vn.nvp.autoclicker.model.CouplePosition
import vn.nvp.autoclicker.model.MyPosition
import java.util.*

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
class FloatingClickService : Service() {
    private lateinit var manager: WindowManager
    private lateinit var view: View
    private lateinit var viewListener: View
    private lateinit var params: WindowManager.LayoutParams
    private var srcStartDragDistance: Int = 0
    private var targetStartDragDistance: Int = 0
    private var timer: Timer? = null
    private var position1: MyPosition? = null
    private var position1Listener: MyPosition? = null
    private val listPosition by lazy {
        CouplePosition.init3Couple(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        srcStartDragDistance = dp2px(5f)
        targetStartDragDistance = dp2px(10f)

        //setting the layout parameters
        val overlayParam =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayParam,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        //getting windows services and adding the floating view to it
        manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addViewPositionClicker()
    }

    private fun viewOnClick(viewSrc: MyPosition?, viewTarget: MyPosition?) {
        val srcLocationTop = viewSrc?.locationX
        val srcLocationLeft = viewSrc?.locationY
        val targetLocationTop = viewTarget?.locationX
        val targetLocationLeft = viewTarget?.locationY

        manager.removeViewImmediate(viewSrc?.view)
        manager.removeViewImmediate(viewTarget?.view)
        autoClickService?.clickDuplicate(
            srcLocationTop ?: 0,
            srcLocationLeft ?: 0,
            targetLocationTop ?: 0,
            targetLocationLeft ?: 0
        )
        manager.addView(viewSrc?.view, viewSrc?.params)
        manager.addView(viewTarget?.view, viewTarget?.params)
    }

    override fun onDestroy() {
        super.onDestroy()
        "FloatingClickService onDestroy".logd()
        removeViewClicker()
    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        "FloatingClickService onConfigurationChanged".logd()
//        val x = params.x
//        val y = params.y
//        params.x = xForRecord
//        params.y = yForRecord
//        xForRecord = x
//        yForRecord = y
//        manager.updateViewLayout(view, params)
//    }

    private fun addViewPositionClicker() {
        listPosition.forEach {
            manager.addView(it.positionSrc.view, it.positionSrc.params)
            manager.addView(it.positionTarget.view, it.positionTarget.params)
        }
        initEventClicker()
    }

    private fun removeViewClicker() {
        listPosition.forEach {
            manager.removeView(it.positionSrc.view)
            manager.removeView(it.positionTarget.view)
        }
    }

    private fun initEventClicker() {
        listPosition.forEach {
            it.positionSrc.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, srcStartDragDistance,
                        { viewOnClick(p, it.positionTarget) },
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }
            it.positionTarget.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, targetStartDragDistance,
                        null,
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }
        }
    }
}
