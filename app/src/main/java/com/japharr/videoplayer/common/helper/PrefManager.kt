package com.japharr.videoplayer.common.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject

/**
 * Created by Japharr on 8/6/2018.
 */
class PrefManager @Inject constructor(var context: Context) {
    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var editor: SharedPreferences.Editor? = null

    init {
        editor = pref.edit()
    }

    fun clean() {
        editor?.clear()
        editor?.commit()
    }

    fun isLoggedIn(): Boolean {
        return false
    }
}