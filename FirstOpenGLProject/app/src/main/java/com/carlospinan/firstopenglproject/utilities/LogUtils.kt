package com.carlospinan.firstopenglproject.utilities

import android.util.Log
import com.carlospinan.firstopenglproject.BuildConfig

private const val LOG_TAG = "OpenGL"

fun log(message: String) {
    if (BuildConfig.LOG) {
        Log.d(LOG_TAG, message)
    }
}