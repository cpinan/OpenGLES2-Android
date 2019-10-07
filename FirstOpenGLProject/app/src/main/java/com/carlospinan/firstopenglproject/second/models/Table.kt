package com.carlospinan.firstopenglproject.second.models

import android.opengl.GLES20.GL_TRIANGLE_FAN
import com.carlospinan.firstopenglproject.second.programs.TextureShaderProgram
import com.carlospinan.firstopenglproject.utilities.gl2DrawArrays

private const val POSITION_COMPONENT_COUNT = 2
private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
private const val STRIDE = (POSITION_COMPONENT_COUNT
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Table {

    private val vertexData = floatArrayOf(
        // Order of coordinates: X, Y, S, T
        // Triangle Fan
        0.0f, 0.0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0.0f, 0.9f,
        0.5f, -0.8f, 1.0f, 0.9f,
        0.5f, 0.8f, 1.0f, 0.1f,
        -0.5f, 0.8f, 0.0f, 0.1f,
        -0.5f, -0.8f, 0.0f, 0.9f
    )

    private val vertexArray = VertexArray(vertexData)

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.aPositionLocation,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        gl2DrawArrays(
            GL_TRIANGLE_FAN,
            0,
            6
        )
    }

}