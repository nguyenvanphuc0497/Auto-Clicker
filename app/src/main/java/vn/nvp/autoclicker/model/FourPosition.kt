package vn.nvp.autoclicker.model

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager
import androidx.annotation.DrawableRes
import vn.nvp.autoclicker.R
import vn.nvp.autoclicker.inflaterViewClicker

/**
 * Create by Nguyen Van Phuc on 12/14/20
 */
data class FourPosition(
    val position1: MyPosition,
    val position2: MyPosition,
    val position3: MyPosition,
    val position4: MyPosition,
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

        private fun inflaterViewForFour(
            context: Context,
            number: Int,
            @DrawableRes icDrawableId: Int,
            yStart: Int,
        ): FourPosition =
            FourPosition(
                position1 = MyPosition(
                    context.inflaterViewClicker("${number}A", icDrawableId),
                    initParamsOfView().apply {
                        x = 0
                        y = yStart
                    }
                ),
                position2 = MyPosition(
                    context.inflaterViewClicker("${number}B", icDrawableId),
                    initParamsOfView().apply {
                        x = 100
                        y = yStart
                    }
                ),
                position3 = MyPosition(
                    context.inflaterViewClicker("${number}C", icDrawableId),
                    initParamsOfView().apply {
                        x = 200
                        y = yStart
                    }
                ),
                position4 = MyPosition(
                    context.inflaterViewClicker("${number}D", icDrawableId),
                    initParamsOfView().apply {
                        x = 300
                        y = yStart
                    }
                )
            )

        fun init3Four(context: Context): List<FourPosition> = listOf(
            inflaterViewForFour(context, 1, R.drawable.ic_plus_blue_900_24dp, 0),
            inflaterViewForFour(
                context,
                2,
                R.drawable.ic_plus_green_900_24dp,
                200
            ),
            inflaterViewForFour(context, 3, R.drawable.ic_plus_red_900_24dp, 400),
        )
    }
}
