package com.carlospinan.firstopenglproject.second.models

import android.opengl.GLES20.GL_FLOAT
import com.carlospinan.firstopenglproject.utilities.gl2EnableVertexAttribArray
import com.carlospinan.firstopenglproject.utilities.gl2VertexAttribPointer
import java.nio.ByteBuffer
import java.nio.ByteOrder

const val BYTES_PER_FLOAT = 4

open class VertexArray(
    vertexData: FloatArray
) {
    private val floatBuffer = ByteBuffer
        .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData)

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer.position(dataOffset)

        gl2VertexAttribPointer(
            index = attributeLocation,
            size = componentCount,
            type = GL_FLOAT,
            normalized = false,
            stride = stride,
            pointer = floatBuffer
        )

        gl2EnableVertexAttribArray(
            index = attributeLocation
        )

        floatBuffer.position(0)

    }
}