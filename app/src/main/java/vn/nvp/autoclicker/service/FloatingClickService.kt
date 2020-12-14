package vn.nvp.autoclicker.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import vn.nvp.autoclicker.TouchAndDragListener
import vn.nvp.autoclicker.dp2px
import vn.nvp.autoclicker.logd
import vn.nvp.autoclicker.model.CouplePosition
import vn.nvp.autoclicker.model.FourPosition
import vn.nvp.autoclicker.model.MyLocation
import vn.nvp.autoclicker.model.MyPosition

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
class FloatingClickService : Service() {
    private lateinit var manager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private var srcStartDragDistance: Int = 0
    private var targetStartDragDistance: Int = 0
    private val listPosition by lazy {
        CouplePosition.init3Couple(this)
    }
    private val listFourPosition by lazy {
        FourPosition.init3Four(this)
    }
    private var modeCurrent: ModeType? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getIntExtra("key_mode_type", 1)?.let {
            parseModeIntentExtra(it)
        }
        return super.onStartCommand(intent, flags, startId)
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
    }

    private fun initViews() {
        if (modeCurrent == ModeType.SINGLE) {
            addViewPositionClicker()
        } else if (modeCurrent == ModeType.MULTI) {
            addViewPositionClickerModeMulti()
        }
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

    private fun viewOnClickMulti(viewPosition: List<MyPosition>) {
        val listLocation = mutableListOf<MyLocation>()

        viewPosition.forEach {
            listLocation.add(MyLocation(it.locationX, it.locationY))
            manager.removeViewImmediate(it.view)
        }
        autoClickService?.clickDuplicateMulti(listLocation)
        viewPosition.forEach {
            manager.addView(it.view, it.params)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        "FloatingClickService onDestroy".logd()
        try {
            removeViewClicker()
        } catch (e: Exception) {
            Log.e("xxx", e.message ?: "")
        }
    }

    private fun addViewPositionClicker() {
        listPosition.forEach {
            manager.addView(it.positionSrc.view, it.positionSrc.params)
            manager.addView(it.positionTarget.view, it.positionTarget.params)
        }
        initEventClicker()
    }

    private fun addViewPositionClickerModeMulti() {
        listFourPosition.forEach {
            manager.addView(it.position1.view, it.position1.params)
            manager.addView(it.position2.view, it.position2.params)
            manager.addView(it.position3.view, it.position3.params)
            manager.addView(it.position4.view, it.position4.params)
        }
        initEventClickerForFourPosition()
    }

    private fun removeViewClicker() {
        if (modeCurrent == ModeType.SINGLE) {
            listPosition.forEach {
                manager.removeView(it.positionSrc.view)
                manager.removeView(it.positionTarget.view)
            }
        } else if (modeCurrent == ModeType.MULTI) {
            removeViewClickerForFourPosition()
        }
    }

    private fun removeViewClickerForFourPosition() {
        listFourPosition.forEach {
            manager.removeView(it.position1.view)
            manager.removeView(it.position2.view)
            manager.removeView(it.position3.view)
            manager.removeView(it.position4.view)
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

    private fun initEventClickerForFourPosition() {
        listFourPosition.forEach {
            it.position1.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, srcStartDragDistance,
                        {
                            viewOnClickMulti(
                                listOf(
                                    it.position1,
                                    it.position2,
                                    it.position3,
                                    it.position4
                                )
                            )
                        },
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }
            it.position2.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, targetStartDragDistance,
                        null,
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }

            it.position3.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, targetStartDragDistance,
                        null,
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }

            it.position4.let { p ->
                p.view.setOnTouchListener(
                    TouchAndDragListener(p.params, targetStartDragDistance,
                        null,
                        { manager.updateViewLayout(p.view, p.params) })
                )
            }
        }
    }

    private fun parseModeIntentExtra(modeInt: Int) {
        if (modeInt == 1) {
            modeCurrent = ModeType.SINGLE
        } else if (modeInt == 2) {
            modeCurrent = ModeType.MULTI
        }
        initViews()
    }
}

enum class ModeType {
    SINGLE,
    MULTI
}
