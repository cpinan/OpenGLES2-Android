package com.carlospinan.firstopenglproject.first

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.firstopenglproject.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val activityManager by lazy {
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportGLEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportGLEs2) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(FirstGLRenderer())
        } else {
            Toast.makeText(
                    this,
                    "This device does not support OpenGL ES 2.0",
                    Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // TODO Change the view to use lifecycle architecture.
    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

}
