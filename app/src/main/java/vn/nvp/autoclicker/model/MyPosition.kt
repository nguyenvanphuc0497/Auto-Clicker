package vn.nvp.autoclicker.model

import android.view.View
import android.view.WindowManager

/**
 * Create by Nguyen Van Phuc on 12/9/20
 */
data class MyPosition(
    val view: View,
    val params: WindowManager.LayoutParams
) {
    private val currentLocationTop: Int?
        get() {
            return IntArray(2).apply {
                view.getLocationOnScreen(this)
            }.getOrNull(0)
        }
    private val currentLocationLeft: Int?
        get() {
            return IntArray(2).apply {
                view.getLocationOnScreen(this)
            }.getOrNull(1)
        }

    val locationX: Int?
        get() = (currentLocationTop ?: 0) + (view.top + view.bottom) / 2

    val locationY: Int?
        get() = (currentLocationLeft ?: 0) + (view.left + view.right) / 2 - 20
}
