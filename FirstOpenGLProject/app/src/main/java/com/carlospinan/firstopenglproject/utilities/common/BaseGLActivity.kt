package com.carlospinan.firstopenglproject.utilities.common

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.firstopenglproject.R
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseGLActivity : AppCompatActivity() {

    private val activityManager by lazy {
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    abstract fun renderer(): BaseGLRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportGLEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportGLEs2) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(renderer())
        } else {
            Toast.makeText(
                this,
                "This device does not support OpenGL ES 2.0",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}