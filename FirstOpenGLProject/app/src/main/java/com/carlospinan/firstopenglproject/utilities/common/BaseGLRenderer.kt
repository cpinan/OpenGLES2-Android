package com.carlospinan.firstopenglproject.utilities.common

import android.opengl.GLSurfaceView
import com.carlospinan.firstopenglproject.utilities.gl2Clear
import com.carlospinan.firstopenglproject.utilities.gl2ClearColor
import com.carlospinan.firstopenglproject.utilities.gl2ViewPort
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author Carlos Piñan
 * @explanation Just used to change the default names.
 * In OpenGL, we can only draw points, lines, and triangles.
 *
 * Endianness is a way of describing how a hardware architecture orders the bits and bytes that make up a number at a low level.
 *
 * A vertex shader generates the final position of each vertex and is run once per vertex.
 * Once the final positions are known, OpenGL will take the visible
 * set of vertices and assemble them into points, lines, and triangles.
 *
 * A fragment shader generates the final color of each fragment of a point,
 * line, or triangle and is run once per fragment. A fragment is a small,
 * rectangular area of a single color, analogous to a pixel on a computer screen.
 *
 */
abstract class BaseGLRenderer : GLSurfaceView.Renderer {

    /**
     * GLSurfaceView calls this when the surface is created.
     * This happens the first time our application is run,
     * and it may also be called when the device wakes up or
     * when the user switches back to our activity.
     * In practice, this means that this method may be called
     * multiple times while our application is running.
     */
    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        gl2ClearColor(red = 1.0f, alpha = 0.0f)
    }

    /**
     * GLSurfaceView calls this after the surface is created and whenever the size has changed.
     * A size change can occur when switching from portrait to landscape and vice versa.
     */
    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        gl2ViewPort(width = width, height = height)
    }

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

}