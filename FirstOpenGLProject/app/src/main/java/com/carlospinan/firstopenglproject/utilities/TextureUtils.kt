package com.carlospinan.firstopenglproject.utilities

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils

/**
 * We generate one texture object by calling glGenTextures(1, textureObjectId, 0),
 * passing in 1 as the first parameter. OpenGL will store the generated IDs in textureObjectIds.
 * We also check that the call to glGenTextures() succeeded by continuing only if it’s not equal to zero
 */
fun Context.loadTexture(resourceId: Int): Int {
    val textureObjectIds = IntArray(1)
    glGenTextures(1, textureObjectIds, 0)
    if (textureObjectIds[0] == 0) {
        log("Could not generate a new OpenGL texture object.")
        return 0
    }
    val options = BitmapFactory.Options().apply {
        inScaled = false
    }
    val bitmap = BitmapFactory.decodeResource(
        resources,
        resourceId,
        options
    )
    if (bitmap == null) {
        log("Resource ID $resourceId could not be decoded.")
        glDeleteTextures(
            1,
            textureObjectIds,
            0
        )
        return 0
    }
    glBindTexture(
        GL_TEXTURE_2D,
        textureObjectIds[0]
    )

    /*
    We set each filter with a call to glTexParameteri():
    GL_TEXTURE_MIN_FILTER refers to minification, while GL_TEXTURE_MAG_FILTER refers to magnification.
    For minification, we select GL_LINEAR_MIPMAP_LINEAR, which tells OpenGL
    to use trilinear filtering. We set the magnification filter to GL_LINEAR,
    which tells OpenGL to use bilinear filtering.
     */
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MIN_FILTER,
        GL_LINEAR_MIPMAP_LINEAR
    )

    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAG_FILTER,
        GL_LINEAR
    )

    /*
        This call tells OpenGL to read in the bitmap data defined by
        bitmap and copy it over into the texture object that is currently bound.
     */
    GLUtils.texImage2D(
        GL_TEXTURE_2D,
        0,
        bitmap,
        0
    )
    bitmap.recycle()
    // Generating mipmaps is also a cinch. We can tell OpenGL to generate
    // all of the necessary levels with a quick call to glGenerateMipmap():
    glGenerateMipmap(GL_TEXTURE_2D)

    /*
        Now that we’ve finished loading the texture, a good practice is to then unbind from the texture
        so that we don’t accidentally make further changes to this texture with other texture calls
     */
    glBindTexture(GL_TEXTURE_2D, 0)

    return textureObjectIds[0]
}