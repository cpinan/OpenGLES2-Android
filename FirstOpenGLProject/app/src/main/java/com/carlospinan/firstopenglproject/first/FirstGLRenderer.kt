package com.carlospinan.firstopenglproject.first

import android.opengl.GLSurfaceView
import com.carlospinan.firstopenglproject.utilities.gl2Clear
import com.carlospinan.firstopenglproject.utilities.gl2ClearColor
import com.carlospinan.firstopenglproject.utilities.gl2ViewPort
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class FirstGLRenderer : GLSurfaceView.Renderer {

    /**
     * GLSurfaceView calls this when it’s time to draw a frame.
     * We must draw something, even if it’s only to gl2Clear the screen.
     * The rendering buffer will be swapped and displayed on the screen
     * after this method returns, so if we don’t draw anything,
     * we’ll probably get a bad flickering effect.
     */
    override fun onDrawFrame(unused: GL10?) {
        gl2Clear()
    }

    /**
     * GLSurfaceView calls this after the surface is created and whenever the size has changed.
     * A size change can occur when switching from portrait to landscape and vice versa.
     */
    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        gl2ViewPort(width = width, height = height)
    }

    /**
     * GLSurfaceView calls this when the surface is created.
     * This happens the first time our application is run,
     * and it may also be called when the device wakes up or
     * when the user switches back to our activity.
     * In practice, this means that this method may be called
     * multiple times while our application is running.
     */
    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        gl2ClearColor(red = 1.0f, alpha = 1.0f)
    }

}