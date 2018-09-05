package com.japharr.videoplayer.common.extension

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager

/**
 * Created by Japharr on 8/5/2018.
 */
fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.getClassName()) {
            return true
        }
    }
    return false
}

fun Context.dpToPx(dp: Float): Int {
    val r = this.resources
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics))
}

fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = this.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun Activity.transparentToolbar() {
    if (Build.VERSION.SDK_INT in 19..20) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    if (Build.VERSION.SDK_INT >= 19) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}