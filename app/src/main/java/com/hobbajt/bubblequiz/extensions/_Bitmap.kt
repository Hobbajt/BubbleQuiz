package com.hobbajt.bubblequiz.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray
{
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    recycle()
    return byteArray
}

fun Bitmap.toIntArray(): IntArray
{
    val pixels = IntArray(width * width)
    getPixels(pixels, 0, width, 0, 0, width, width)
    return pixels
}

fun ByteArray.toBitmap(): Bitmap
{
    return BitmapFactory.decodeByteArray(this, 0, size)
}
