package com.hobbajt.bubblequiz.utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class BitmapUtilities
{
    companion object
    {
        fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray
        {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            bitmap.recycle()
            return byteArray
        }

        fun convertByteArrayToBitmap(bytes: ByteArray): Bitmap
        {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}