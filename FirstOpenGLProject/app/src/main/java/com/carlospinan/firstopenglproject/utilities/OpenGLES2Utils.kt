package com.carlospinan.firstopenglproject.utilities

import android.opengl.GLES20.*
import java.nio.Buffer

/**
 * android.opengl.GLES20
 * android.opengl.GLUtils
 * android.opengl.Matrix
 */

fun gl2ClearColor(
    red: Float = 0.0f,
    green: Float = 0.0f,
    blue: Float = 0.0f,
    alpha: Float = 1.0f
) {
    glClearColor(red, green, blue, alpha)
}

fun gl2ViewPort(
    x: Int = 0,
    y: Int = 0,
    width: Int,
    height: Int
) {
    glViewport(x, y, width, height)
}

fun gl2Clear(value: Int = GL_COLOR_BUFFER_BIT) {
    glClear(value)
}

fun gl2UseProgram(program: Int) {
    glUseProgram(program)
}

fun gl2GetUniformLocation(program: Int, name: String): Int {
    return glGetUniformLocation(program, name)
}

fun gl2GetAttribLocation(program: Int, name: String): Int {
    return glGetAttribLocation(program, name)
}

// glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, Buffer ptr)
fun gl2VertexAttribPointer(
    index: Int = 0,
    size: Int = 0,
    type: Int = 0,
    normalized: Boolean = false,
    stride: Int = 0,
    pointer: Buffer
) {
    glVertexAttribPointer(
        index,
        size,
        type,
        normalized,
        stride,
        pointer
    )
}

fun gl2EnableVertexAttribArray(index: Int) {
    glEnableVertexAttribArray(index)
}

fun gl2Uniform4f(
    location: Int = 0,
    red: Float = 0.0f,
    green: Float = 0.0f,
    blue: Float = 0.0f,
    alpha: Float = 0.0f
) {
    glUniform4f(location, red, green, blue, alpha)
}

fun gl2DrawArrays(
    mode: Int,
    first: Int,
    count: Int
) {
    glDrawArrays(mode, first, count)
}