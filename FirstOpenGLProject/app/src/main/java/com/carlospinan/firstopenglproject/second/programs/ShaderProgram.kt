package com.carlospinan.firstopenglproject.second.programs

import android.content.Context
import com.carlospinan.firstopenglproject.utilities.buildProgram
import com.carlospinan.firstopenglproject.utilities.gl2UseProgram
import com.carlospinan.firstopenglproject.utilities.readTextFileFromResource

// UNIFORM CONSTANTS
const val U_MATRIX = "u_Matrix"
const val U_TEXTURE_UNIT = "u_TextureUnit"

// ATTRIBUTE CONSTANTS
const val A_POSITION = "a_Position"
const val A_COLOR = "a_Color"
const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

abstract class ShaderProgram(
    val context: Context,
    private val vertexShaderResourceId: Int,
    private val fragmentShaderResourceId: Int
) {

    private var program: Int? = null

    fun getProgram(): Int {
        if (program == null) {
            program = buildProgram(
                context.readTextFileFromResource(vertexShaderResourceId),
                context.readTextFileFromResource(fragmentShaderResourceId)
            )
        }
        return program!!
    }

    fun useProgram() {
        gl2UseProgram(getProgram())
    }

    abstract fun setUniforms(matrix: FloatArray = floatArrayOf(), textureId: Int = 0)

}