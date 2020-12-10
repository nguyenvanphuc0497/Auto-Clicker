package vn.nvp.autoclicker

import android.content.Context
import android.util.Log
import androidx.constraintlayout.solver.LinearSystem.DEBUG

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
private const val TAG = "AutoClickService"

fun Any.logd(tag: String = TAG) {
//    if (!DEBUG) return
    if (this is String) {
        Log.d(tag, this)
    } else {
        Log.d(tag, this.toString())
    }
}

fun Any.loge(tag: String = TAG) {
//    if (!DEBUG) return
    if (this is String) {
        Log.e(tag, this)
    } else {
        Log.e(tag, this.toString())
    }
}

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

typealias Action = () -> Unit
