package com.carlospinan.firstopenglproject.second

import com.carlospinan.firstopenglproject.utilities.common.BaseGLActivity
import com.carlospinan.firstopenglproject.utilities.common.BaseGLRenderer

class AirHockeyActivity : BaseGLActivity() {

    override fun renderer(): BaseGLRenderer {
        return AirHockeyRenderer(this)
    }

}