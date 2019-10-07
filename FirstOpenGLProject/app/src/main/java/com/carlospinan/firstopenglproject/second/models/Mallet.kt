package com.carlospinan.firstopenglproject.second.models

import android.opengl.GLES20.GL_POINTS
import com.carlospinan.firstopenglproject.second.programs.ColorShaderProgram
import com.carlospinan.firstopenglproject.utilities.gl2DrawArrays

private const val POSITION_COMPONENT_COUNT = 2
private const val COLOR_COMPONENT_COUNT = 3
private const val STRIDE = (POSITION_COMPONENT_COUNT
        + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Mallet {

    private val vertexData = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )

    private val vertexArray = VertexArray(vertexData)

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            colorProgram.aColorLocation,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        gl2DrawArrays(
            GL_POINTS,
            0,
            2
        )
    }

}