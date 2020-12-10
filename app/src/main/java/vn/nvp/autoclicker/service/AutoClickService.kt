package vn.nvp.autoclicker.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import vn.nvp.autoclicker.bean.Event
import vn.nvp.autoclicker.logd


/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
var autoClickService: AutoClickService? = null

class AutoClickService : AccessibilityService() {

    internal val events = mutableListOf<Event>()

    override fun onInterrupt() {
        // NO-OP
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        event.eventType.logd("onAccessibilityEvent")
//        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.eventType) {
//            val nodeInfo = event.source
//            nodeInfo.logd("nodeInfo")
//            if (nodeInfo == null) {
//                return
//            }
//            var list = nodeInfo
//                .findAccessibilityNodeInfosByViewId("com.android.settings:id/btnListener")
//            for (node in list) {
//                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//            }
//            list = nodeInfo
//                .findAccessibilityNodeInfosByViewId("android:id/start")
//            for (node in list) {
//                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//            }
//        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        "onServiceConnected".logd()
        autoClickService = this

        val info = AccessibilityServiceInfo()
        info.flags =
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS

        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.packageNames = arrayOf("vn.nvp.autoclicker")
        serviceInfo = info

//        startActivity(
//            Intent(this, MainActivity::class.java)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        )
    }

    fun click(x: Int, y: Int) {
        "click $x $y".logd()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        val path = Path()
        path.moveTo(x.toFloat(), y.toFloat())
        val builder = GestureDescription.Builder()
        val gestureDescription = builder
            .addStroke(GestureDescription.StrokeDescription(path, 0, 2))
            .build()
        dispatchGesture(gestureDescription, null, null).toString().logd()
    }

    fun clickDuplicate(xSrc: Int, ySrc: Int, xTarget: Int, yTarget: Int) {
        "source: $xSrc $ySrc, target: $xTarget $yTarget".logd()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        val pathSrc = Path().apply {
            moveTo(xSrc.toFloat(), ySrc.toFloat())
        }
        val pathTarget = Path().apply {
            moveTo(xTarget.toFloat(), yTarget.toFloat())
        }
        val builder = GestureDescription.Builder()
        val gestureDescription = builder
            .addStroke(GestureDescription.StrokeDescription(pathTarget, 5, 2))
            .addStroke(GestureDescription.StrokeDescription(pathSrc, 0, 2))
            .build()
        dispatchGesture(gestureDescription, null, null).toString().logd()
    }

    fun run(newEvents: MutableList<Event>) {
        events.clear()
        events.addAll(newEvents)
        events.toString().logd()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        val builder = GestureDescription.Builder()
        events.forEach { builder.addStroke(it.onEvent()) }
        dispatchGesture(builder.build(), null, null)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "AutoClickService onUnbind".logd()
        autoClickService = null
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        "AutoClickService onDestroy".logd()
        autoClickService = null
        super.onDestroy()
    }

}
