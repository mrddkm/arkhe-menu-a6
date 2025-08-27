package com.arkhe.menu.presentation.utils

import android.util.Log
import com.arkhe.menu.BuildConfig

object Logger {
    private const val TAG = "ArkheMenu"

    fun d(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun i(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun w(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }
}