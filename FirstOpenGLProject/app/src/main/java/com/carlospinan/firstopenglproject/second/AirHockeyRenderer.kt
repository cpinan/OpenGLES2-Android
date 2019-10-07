package com.carlospinan.firstopenglproject.second

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.Matrix
import com.carlospinan.firstopenglproject.R
import com.carlospinan.firstopenglproject.second.models.BYTES_PER_FLOAT
import com.carlospinan.firstopenglproject.second.models.Mallet
import com.carlospinan.firstopenglproject.second.models.Table
import com.carlospinan.firstopenglproject.second.programs.ColorShaderProgram
import com.carlospinan.firstopenglproject.second.programs.TextureShaderProgram
import com.carlospinan.firstopenglproject.utilities.*
import com.carlospinan.firstopenglproject.utilities.common.BaseGLRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val POSITION_COMPONENT_COUNT = 4
private const val COLOR_COMPONENT_COUNT = 3
private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

//private const val U_COLOR = "u_Color"
private const val A_POSITION = "a_Position"
private const val A_COLOR = "a_Color"
private const val U_MATRIX = "u_Matrix"

/**
 * A triangle fan begins with a center vertex, using the next two vertices to create the first triangle.
 * Each subsequent vertex will create another triangle, fanning around the original center point.
 * To complete the fan, we just repeat the second point at the end.
 *
 * The focal length is calculated by 1/tangent of (field of vision/2). The field of vision must be less than 180 degrees.
 *
 * https://developer.android.com/reference/android/opengl/package-summary
 */
class AirHockeyRenderer(
    private val context: Context
) : BaseGLRenderer() {

    private var program: Int = 0
    // private var uColorLocation: Int = 0
    private var aPositionLocation: Int = 0
    private var aColorLocation: Int = 0
    private var uMatrixLocation: Int = 0

    private var vertexData: FloatBuffer
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private lateinit var table: Table
    private lateinit var mallet: Mallet

    private lateinit var textureShaderProgram: TextureShaderProgram
    private lateinit var colorShaderProgram: ColorShaderProgram
    private var texture: Int = 0

    // BOTTOM-LEFT ; TOP-LEFT ; TOP-RIGHT ; BOTTOM-RIGHT
    /*
    private val tableVertices = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 14.0f,
        9.0f, 14.0f,
        9.0f, 0.0f
    )
     */

    /*
    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1: BOTTOM-LEFT ; TOP-RIGHT ; TOP-LEFT
        0.0f, 0.0f,
        9.0f, 14.0f,
        0.0f, 14.0f,
        // Triangle 2: BOTTOM-LEFT ; BOTTOM-RIGHT ; TOP-RIGHT
        0.0f, 0.0f,
        9.0f, 0.0f,
        9.0f, 14.0f,
        // Line 1: LEFT ; RIGHT
        0.0f, 7.0f,
        9.0f, 7.0f,
        // Mallets: BOTTOM ; TOP (single points)
        4.5f, 2.0f,
        4.5f, 12.0f
    )
     */
    /*
    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B == POS_COMP = 2
        // // Order of coordinates: X, Y, Z, W, R, G, B == POS_COMP = 4
        // Triangle Fan
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,
        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )
    */
    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, Z, W, R, G, B
        // Triangle Fan
        0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
        0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
        0f, 0.4f, 0f, 1.75f, 1f, 0f, 0f
    )

    init {
        /**
         * First we allocated a block of native memory using ByteBuffer.allocateDirect();
         * this memory will not be managed by the garbage collector.
         *
         * We need to tell the method how large the block of memory should be in bytes.
         * Since our vertices are stored in an array of floats and there are 4 bytes per float,
         * we pass in tableVerticesWithTriangles.length * BYTES_PER_FLOAT.
         *
         * The next line tells the byte buffer that it should organize its bytes in native order.
         * When it comes to values that span multiple bytes, such as 32-bit integers,
         * the bytes can be ordered either from most significant to least signif- icant or from least to most.
         * Think of this as similar to writing a number either from left to right or right to left.
         * It’s not important for us to know what that order is,
         * but it is important that we use the same order as the platform.
         * We do this by calling order(ByteOrder.nativeOrder()).
         *
         */
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        gl2ClearColor(alpha = 0.0f)

        textureShaderProgram = TextureShaderProgram(context)
        colorShaderProgram = ColorShaderProgram(context)

        table = Table()
        mallet = Mallet()

        texture = context.loadTexture(R.drawable.air_hockey_surface)

        /*
        val vertexShaderSource = context.readTextFileFromResource(R.raw.simple_vertex_shader)
        val fragmentShaderSource = context.readTextFileFromResource(R.raw.simple_fragment_shader)

        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        program = linkProgram(vertexShader, fragmentShader)

        log("${validateProgram(program)}")
        gl2UseProgram(program)

        // uColorLocation = gl2GetUniformLocation(program, U_COLOR)
        aColorLocation = gl2GetAttribLocation(program, A_COLOR)
        aPositionLocation = gl2GetAttribLocation(program, A_POSITION)
        uMatrixLocation = gl2GetUniformLocation(program, U_MATRIX)

        vertexData.position(0)
        // After calling glVertexAttribPointer(), OpenGL now knows where to read the data for the attribute a_Position.
        gl2VertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        gl2EnableVertexAttribArray(aPositionLocation)

        vertexData.position(POSITION_COMPONENT_COUNT)
        // Associate color to shader
        gl2VertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )

        // Associate color to shader
        gl2EnableVertexAttribArray(aColorLocation)
         */

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        gl2ViewPort(width = width, height = height)

        /*
        val aspectRatio = if (width > height)
            width.toFloat() / height.toFloat()
        else
            height.toFloat() / width.toFloat()

        if (width > height) {
            // Landscape
            orthographic(
                destinationArray = projectionMatrix,
                mOffset = 0,
                left = -aspectRatio,
                right = aspectRatio,
                bottom = -1.0f,
                top = 1.0f,
                near = -1.0f,
                far = 1.0f
            )
        } else {
            // Portrait or square
            orthographic(
                destinationArray = projectionMatrix,
                mOffset = 0,
                left = -1.0f,
                right = 1.0f,
                bottom = -aspectRatio,
                top = aspectRatio,
                near = -1.0f,
                far = 1.0f
            )
        }
        */

        perspectiveM(
            destinationArray = projectionMatrix,
            offset = 0,
            fovY = 45.0f,
            aspect = width.toFloat() / height.toFloat(),
            zNear = 1.0f,
            zFar = 10.0f
        )
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(
            modelMatrix,
            0,
            0.0f,
            0.0f,
            -2.5f
        )
        Matrix.rotateM(
            modelMatrix,
            0,
            -60.0f,
            1.0f,
            0.0f,
            0.0f
        )

        val temp = FloatArray(16)
        Matrix.multiplyMM(
            temp,
            0,
            projectionMatrix,
            0,
            modelMatrix,
            0
        )
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)

    }

    override fun onDrawFrame(unused: GL10?) {
        // gl2Clear()
        // Clear the rendering surface.
        gl2Clear(GL_COLOR_BUFFER_BIT)

        // Draw the table.
        textureShaderProgram.useProgram()
        textureShaderProgram.setUniforms(projectionMatrix, texture)
        table.bindData(textureShaderProgram)
        table.draw()

        // Draw the mallets
        colorShaderProgram.useProgram()
        colorShaderProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorShaderProgram)
        mallet.draw()

        // gl2UniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)

        //gl2Uniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        //gl2DrawArrays(GL_TRIANGLES, 11, 6)

        /**
         * First we update the value of u_Color in our shader code by calling glUniform4f().
         * Unlike attributes, uniforms don’t have default components,
         * so if a uniform is defined as a vec4 in our shader, we need to provide all four components.
         * We want to start out by drawing a white table, so we set red, green, and blue to 1.0f for full brightness.
         * The alpha value doesn’t matter, but we still need to specify it since a color has four components.
         */
        // gl2Uniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)

        /**
         * Once we’ve specified the color, we then draw our table with a call to glDrawArrays(GLES20.GL_TRIANGLES, 0, 6).
         * The first argument tells OpenGL that we want to draw triangles.
         * To draw triangles, we need to pass in at least three vertices per triangle.
         * The second argument tells OpenGL to read in vertices starting at the beginning of our vertex array,
         * and the third argument tells OpenGL to read in six vertices.
         * Since there are three vertices per triangle, this call will end up drawing two triangles.
         */
        //gl2DrawArrays(GL_TRIANGLES, 0, 6)
        //gl2DrawArrays(GL_TRIANGLE_FAN, 0, 6)

        /**
         * We set the color to red by passing in 1.0f to the first component (red) and 0.0f to green and blue.
         * This time we also ask OpenGL to draw lines. We start six vertices after the first vertex and ask OpenGL
         * to draw lines by reading in two vertices.
         * Just like with Java arrays, we’re using zero-based numbering here: 0, 1, 2, 3, 4, 5, 6
         * means that the number 6 corresponds to six vertices after the first vertex, or the seventh vertex.
         * Since there are two vertices per line, we’ll end up drawing one line using these positions:
         */
        // gl2Uniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        //gl2DrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet in blue
        // gl2Uniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        //gl2DrawArrays(GL_POINTS, 8, 1)

        // Draw the first mallet in red
        // gl2Uniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        //gl2DrawArrays(GL_POINTS, 9, 1)

        // CHALLENGES
        // Pock center in Green
        //gl2Uniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f)
        //gl2DrawArrays(GL_POINTS, 10, 1)

    }

}