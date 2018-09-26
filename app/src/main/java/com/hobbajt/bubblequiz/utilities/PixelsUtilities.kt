package com.hobbajt.bubblequiz.utilities

import android.graphics.Color
import com.hobbajt.bubblequiz.photo.model.dto.Position
import java.util.*

class PixelsUtilities
{
    companion object
    {
        fun parse2DArrayTo1D(array : Array<IntArray>) : IntArray
        {
            val pixels1D = IntArray(array.size * array[0].size)

            for (i in array.indices)
            {
                System.arraycopy(array[i], 0, pixels1D, i * array[0].size, array[i].size)
            }

            return pixels1D
        }

        fun parse1DArrayTo2D(array: IntArray): Array<IntArray>
        {
            val size = Math.sqrt(array.size.toDouble()).toInt()
            val pixels2D = Array(size) { IntArray(size) }

            for (i in 0 until size)
            {
                System.arraycopy(array, i * size, pixels2D[i], 0, size)
            }

            return pixels2D
        }

        /**
         * Calculates average color of all pixels
         * @param pixels    Array of pixels
         *
         * @return          Average color of all pixels
         */
        fun calculateAverageColor(pixels : IntArray) : Int
        {
            var red = 0
            var green = 0
            var blue = 0

            var pixelsCount = 0
            for (color in pixels)
            {
                red += Color.red(color)
                green += Color.green(color)
                blue += Color.blue(color)
                pixelsCount++
            }

            return Color.rgb(red / pixelsCount, green / pixelsCount, blue / pixelsCount)
        }

        fun selectPartOf2DArray(source: Array<IntArray>, position: Position, size: Int): Array<IntArray>
        {
            val atomicPixels = Array(size) { IntArray(size) }
            var y = position.y
            var x = 0
            while (y < position.y + size)
            {
                atomicPixels[x] = Arrays.copyOfRange(source[y], position.x, position.x + size)
                y++
                x++
            }
            return atomicPixels
        }
    }
}