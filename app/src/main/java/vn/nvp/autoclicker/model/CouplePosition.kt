package vn.nvp.autoclicker.model

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import vn.nvp.autoclicker.R

/**
 * Create by Nguyen Van Phuc on 12/10/20
 */
data class CouplePosition(
    val positionSrc: MyPosition,
    val positionTarget: MyPosition,
) {
    companion object {
        private fun initParamsOfView() =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_PHONE
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

        private fun inflaterView(
            context: Context,
            text: String,
            @DrawableRes icDrawableId: Int
        ): View = LayoutInflater.from(context).inflate(R.layout.postion_clicker, null).apply {
            if (this is TextView) {
                this.text = text
                this.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    ResourcesCompat.getDrawable(
                        resources,
                        icDrawableId,
                        null
                    ),
                    null,
                    null
                )
            }
        }

        private fun inflaterViewForCouple(
            context: Context,
            number: Int,
            @DrawableRes icDrawableId: Int,
            yStart: Int,
        ): CouplePosition =
            CouplePosition(
                positionSrc = MyPosition(
                    inflaterView(context, "${number}A", icDrawableId),
                    initParamsOfView().apply {
                        x = 0
                        y = yStart
                    }
                ),
                positionTarget = MyPosition(
                    inflaterView(context, "${number}B", icDrawableId),
                    initParamsOfView().apply {
                        x = 200
                        y = yStart
                    }
                )
            )

        fun init3Couple(context: Context): List<CouplePosition> = listOf(
            inflaterViewForCouple(context, 1, R.drawable.ic_plus_blue_900_24dp, 0),
            inflaterViewForCouple(context, 2, R.drawable.ic_plus_green_900_24dp, 200),
            inflaterViewForCouple(context, 3, R.drawable.ic_plus_red_900_24dp, 400),
        )
    }
}
