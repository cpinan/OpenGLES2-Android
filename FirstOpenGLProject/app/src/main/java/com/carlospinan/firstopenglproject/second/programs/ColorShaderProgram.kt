package com.carlospinan.firstopenglproject.second.programs

import android.content.Context
import com.carlospinan.firstopenglproject.R
import com.carlospinan.firstopenglproject.utilities.gl2GetAttribLocation
import com.carlospinan.firstopenglproject.utilities.gl2GetUniformLocation
import com.carlospinan.firstopenglproject.utilities.gl2UniformMatrix4fv

class ColorShaderProgram(context: Context) : ShaderProgram(
    context,
    R.raw.simple_vertex_shader,
    R.raw.simple_fragment_shader
) {

    // Retrieve uniform locations for the shader program.
    private val uMatrixLocation = gl2GetUniformLocation(getProgram(), U_MATRIX)

    // Retrieve attribute locations for the shader program.
    val aPositionLocation = gl2GetAttribLocation(getProgram(), A_POSITION)
    val aColorLocation = gl2GetAttribLocation(getProgram(), A_COLOR)

    override fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        gl2UniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )
    }

}