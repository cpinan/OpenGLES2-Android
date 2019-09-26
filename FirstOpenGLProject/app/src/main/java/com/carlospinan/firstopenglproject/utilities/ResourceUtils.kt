package com.carlospinan.firstopenglproject.utilities

import android.content.Context
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.Reader

fun Context.readTextFileFromResource(resourceId: Int): String {
    val body = StringBuilder()
    this.apply {
        val stream = resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(stream)
        val bufferedReader = BufferedReader(inputStreamReader as Reader?)
        var newLine: String? = bufferedReader.readLine()
        while (newLine != null) {
            body.append(newLine).append("\n")
            newLine = bufferedReader.readLine()
        }
    }
    return body.toString()
}

fun Context.readTextFileFromResource2(resourceId: Int): String {
    val body = StringBuilder()
    this.apply {
        val stream = resources.openRawResource(resourceId)
        val buffer = ByteArray(stream.available())
        stream.read(buffer)
        val byteStream = ByteArrayOutputStream()
        byteStream.write(buffer)
        byteStream.close()
        stream.close()
        body.append(byteStream.toString())
    }
    return body.toString()
}
