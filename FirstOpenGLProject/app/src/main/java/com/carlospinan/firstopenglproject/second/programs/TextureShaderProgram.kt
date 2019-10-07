package com.carlospinan.firstopenglproject.second.programs

import android.content.Context
import android.opengl.GLES20.*
import com.carlospinan.firstopenglproject.R
import com.carlospinan.firstopenglproject.utilities.gl2GetAttribLocation
import com.carlospinan.firstopenglproject.utilities.gl2GetUniformLocation
import com.carlospinan.firstopenglproject.utilities.gl2UniformMatrix4fv

class TextureShaderProgram(context: Context) : ShaderProgram(
    context,
    R.raw.texture_vertex_shader,
    R.raw.texture_fragment_shader
) {

    // Retrieve uniform locations for the shader program.
    private val uMatrixLocation = gl2GetUniformLocation(getProgram(), U_MATRIX)
    private val uTextureUnitLocation = gl2GetUniformLocation(getProgram(), U_TEXTURE_UNIT)

    // Retrieve attribute locations for the shader program.
    val aPositionLocation = gl2GetAttribLocation(getProgram(), A_POSITION)
    val aTextureCoordinatesLocation = gl2GetAttribLocation(getProgram(), A_TEXTURE_COORDINATES)

    override fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        gl2UniformMatrix4fv(
            uMatrixLocation,
            1,
            false,
            matrix,
            0
        )

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId)

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0)
    }

}