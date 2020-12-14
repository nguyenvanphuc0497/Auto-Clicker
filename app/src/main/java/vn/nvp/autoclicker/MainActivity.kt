package vn.nvp.autoclicker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import vn.nvp.autoclicker.service.FloatingClickService
import vn.nvp.autoclicker.service.autoClickService


class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_CODE = 141
    }

    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start?.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                || Settings.canDrawOverlays(this)
            ) {
                serviceIntent = Intent(
                    this@MainActivity,
                    FloatingClickService::class.java
                )
                startService(serviceIntent)
                onBackPressed()
            } else {
                askPermission()
                shortToast("You need System Alert Window Permission to do this")
            }
        }
    }

    private fun checkAccess(): Boolean {
        val logTag = "checkAccess"
        val accessName = getString(R.string.accessibility_service_id)
        var accessibilityEnabled = 0

        try {
            accessibilityEnabled =
                Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            Log.d(logTag, "ACCESSIBILITY: $accessibilityEnabled")
        } catch (e: SettingNotFoundException) {
            Log.d(logTag, "Error finding setting, default accessibility to not found: " + e.message)
        }

        if (accessibilityEnabled == 1) {
            Log.d(logTag, "***ACCESSIBILIY IS ENABLED***: ")
            val settingValue = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            Log.d(logTag, "Setting: $settingValue")
            if (settingValue != null) {
                if (settingValue.contains(accessName, true)) {
                    Log.d(logTag, "We've found the correct setting - accessibility is switched on!")
                    return true
                }
            }
            Log.d(logTag, "***END***")
        } else {
            Log.d(logTag, "***ACCESSIBILIY IS DISABLED***")
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        val hasPermission = checkAccess()
        "has access? $hasPermission".logd()
        if (!hasPermission) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, PERMISSION_CODE)
    }

    override fun onDestroy() {
        serviceIntent?.let {
            "stop floating click service".logd()
            stopService(it)
        }
        autoClickService?.let {
            "stop auto click service".logd()
            it.stopSelf()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return it.disableSelf()
            autoClickService = null
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
