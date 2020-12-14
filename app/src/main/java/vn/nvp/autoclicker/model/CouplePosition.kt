package vn.nvp.autoclicker.model

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager
import androidx.annotation.DrawableRes
import vn.nvp.autoclicker.R
import vn.nvp.autoclicker.inflaterViewClicker

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

        private fun inflaterViewForCouple(
            context: Context,
            number: Int,
            @DrawableRes icDrawableId: Int,
            yStart: Int,
        ): CouplePosition =
            CouplePosition(
                positionSrc = MyPosition(
                    context.inflaterViewClicker("${number}A", icDrawableId),
                    initParamsOfView().apply {
                        x = 0
                        y = yStart
                    }
                ),
                positionTarget = MyPosition(
                    context.inflaterViewClicker("${number}B", icDrawableId),
                    initParamsOfView().apply {
                        x = 200
                        y = yStart
                    }
                )
            )

        fun init3Couple(context: Context): List<CouplePosition> = listOf(
            inflaterViewForCouple(context, 1, R.drawable.baseline_control_point_blue_900_36dp, 0),
            inflaterViewForCouple(context, 2, R.drawable.baseline_control_point_green_900_36dp, 200),
            inflaterViewForCouple(context, 3, R.drawable.baseline_control_point_red_900_36dp, 400),
        )
    }
}
