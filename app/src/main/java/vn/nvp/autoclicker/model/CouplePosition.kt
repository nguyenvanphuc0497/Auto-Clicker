package vn.nvp.autoclicker.model

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
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

        fun init3Couple(context: Context): List<CouplePosition> = listOf(
            CouplePosition(
                positionSrc = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "1A"
                        }
                    },
                    initParamsOfView().apply {
                        x = 0
                    }
                ),
                positionTarget = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "1B"
                        }
                    },
                    initParamsOfView().apply {
                        x = 200
                    }
                )
            ),
            CouplePosition(
                positionSrc = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "2A"
                        }
                    },
                    initParamsOfView().apply {
                        x = 0
                        y = 200
                    }
                ),
                positionTarget = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "2B"
                        }
                    },
                    initParamsOfView().apply {
                        x = 200
                        y = 200
                    }
                )
            ),
            CouplePosition(
                positionSrc = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "3A"
                        }
                    },
                    initParamsOfView().apply {
                        x = 0
                        y = 400
                    }
                ),
                positionTarget = MyPosition(
                    LayoutInflater.from(context).inflate(R.layout.postion_src, null).apply {
                        if (this is TextView) {
                            this.text = "3B"
                        }
                    },
                    initParamsOfView().apply {
                        x = 200
                        y = 400
                    }
                )
            )
        )
    }
}
