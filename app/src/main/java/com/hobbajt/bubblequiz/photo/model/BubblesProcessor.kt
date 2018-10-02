package com.hobbajt.bubblequiz.photo.model

import android.graphics.*
import android.util.Log
import com.hobbajt.bubblequiz.photo.model.dto.*
import com.hobbajt.bubblequiz.utilities.PixelsUtilities
import com.hobbajt.bubblequiz.utilities.toBitmap
import com.hobbajt.bubblequiz.utilities.toIntArray
import org.apache.commons.lang3.ArrayUtils
import java.util.*


class BubblesProcessor(imageBytes: ByteArray, screenWidth: Int)
{
    companion object
    {
        private const val MAX_CELLS_SIDE_COUNT = 64
        private const val IMAGE_SIDE = 64
    }

    private val smallBubbleSizePx = screenWidth / IMAGE_SIDE
    private val smallBubblesColors: Array<IntArray>

    private val bigBubblesColors: Array<IntArray>

    private val originalBitmap: Bitmap = imageBytes.toBitmap()

    private var sizeMap = Array(64) { IntArray(64) }
    private var bubblesSet = BubblesSet()

    init
    {
        initSizeMap()
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, MAX_CELLS_SIDE_COUNT, MAX_CELLS_SIDE_COUNT, false)
        val pixels = scaledBitmap.toIntArray()
        smallBubblesColors = PixelsUtilities.parse1DArrayTo2D(pixels)
        bigBubblesColors = convertPixelsToBigBubblesColors(smallBubblesColors)
    }

    fun getBubblesSet(): BubblesSet
    {
        if(bubblesSet.bubbles.isEmpty())
        {
            bubblesSet.bubbles.addAll(createBigBubbles())
        }
        return bubblesSet
    }

    fun setBubblesState(bubblesState: BubblesState)
    {
        this.sizeMap = bubblesState.sizeMap
        this.bubblesSet = bubblesState.bubblesSet
    }

    fun getBubblesState() = BubblesState(sizeMap, bubblesSet)

    private fun initSizeMap()
    {
        for (x in sizeMap.indices)
        {
            for (y in sizeMap.indices)
            {
                sizeMap[x][y] = 8
            }
        }
    }

    private fun convertPixelsToBigBubblesColors(pixels: Array<IntArray>): Array<IntArray>
    {
        val averagePixels = Array(8) { IntArray(8) }

        for (x in 0 until averagePixels[0].size)
        {
            for (y in averagePixels.indices)
            {
                val selectedPixels = selectPartOfArray(pixels, x, y, 8)
                averagePixels[x][y] = PixelsUtilities.calculateAverageColor(selectedPixels)
            }
        }

        return averagePixels
    }

    private fun selectPartOfArray(source : Array<IntArray>, x : Int, y : Int, sideSize : Int) : IntArray
    {
        var selectedPixels = IntArray(0)
        for (i in y * sideSize until y * sideSize + sideSize)
        {
            selectedPixels = ArrayUtils.addAll(selectedPixels, *Arrays.copyOfRange(source[i], x * sideSize, x * sideSize + sideSize))
        }
        return selectedPixels
    }

    private fun createBigBubbles(): MutableList<Bubble>
    {
        val mainBubbles: MutableList<Bubble> = mutableListOf()
        for (x in bigBubblesColors.indices)
        {
            for (y in bigBubblesColors.indices)
            {
                val position = Position(x * 8, y * 8)
                val colors = arrayOf(intArrayOf(bigBubblesColors[x][y]))
                val bubble = createColorBubble(position, colors, true, 8 * smallBubbleSizePx)
                mainBubbles.add(bubble)
            }
        }
        return mainBubbles
    }

    private fun cropBitmapToCircle(bitmap: Bitmap): Bitmap
    {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 255, 255, 255)
        paint.color = 0xff424242.toInt()
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    /**
     * Selects rectangle part of original image.
     *
     *  @param startX       X coordinate of upper left corner of image part
     *  @param startY       Y coordinate of upper left corner of image part
     *  @param size         Width and height of image part expressed in pixels
     *
     *  @return             2D array containing part of original image
     */
    private fun selectPartOfOriginalImage(startX: Int, startY: Int, size: Int): Array<IntArray>
    {
        val pixels = PixelsUtilities.parse1DArrayTo2D(originalBitmap.toIntArray())
        val position = Position((startX + 1) * smallBubbleSizePx, (startY + 1) * smallBubbleSizePx)
        return PixelsUtilities.selectPartOf2DArray(pixels, position, size * smallBubbleSizePx)
    }

    private fun createImageBubble(position: Position, pixels: Array<IntArray>, isChangeable: Boolean): Bubble
    {
        val bubbleBitmap: Bitmap = createBubbleColorBitmap(pixels)
        val pixels1D = bubbleBitmap.toIntArray()
        val positionPx = Position(position.x * smallBubbleSizePx, position.y * smallBubbleSizePx)
        return ImageBubble(pixels1D, position, positionPx, isChangeable)
    }

    private fun createColorBubble(position: Position, pixels: Array<IntArray>, isChangeable: Boolean, sizePx: Int = smallBubbleSizePx): ColorBubble
    {
        val color = PixelsUtilities.calculateAverageColor(PixelsUtilities.parse2DArrayTo1D(pixels))
        val positionPx = Position(position.x * smallBubbleSizePx, position.y * smallBubbleSizePx)
        return ColorBubble(color, sizePx / 2, position, positionPx, isChangeable)
    }

    /**
     * Creates circle pixels from image
     * @param pixels        2D array of pixels
     *
     * @return              Created pixels
     */
    private fun createBubbleColorBitmap(pixels: Array<IntArray>): Bitmap
    {
        val size = pixels.size
        val pixels1D = PixelsUtilities.parse2DArrayTo1D(pixels)
        val bitmap = Bitmap.createBitmap(pixels1D, size, size, Bitmap.Config.ARGB_8888)
        return cropBitmapToCircle(bitmap)
    }

    fun useBomb(point: Position): Boolean
    {
        var isBombUsed = false
        val atomicPixels = PixelsUtilities.selectPartOf2DArray(smallBubblesColors, point, 24)
        val bubblesToAdd = BubblesSet()
        val bubblesToRemove = BubblesSet()

        for (x in point.x until point.x + 24)
        {
            for (y in point.y until point.y + 24)
            {
                //val positionPx = Position(x * smallBubbleSizePx, y * smallBubbleSizePx)
                val position = Position(x, y)
                if (bubblesSet.isChangeable(position) && sizeMap[x][y] > 1)
                {
                    isBombUsed = true
                    sizeMap[x][y] = 1
                    val partPixels = arrayOf(intArrayOf(atomicPixels[y - point.y][x - point.x]))
                    val bubble = createColorBubble(Position(x, y), partPixels, true, smallBubbleSizePx)
                    Log.d("test", "${bubble.radiusPx}   ${bubble.positionPx.x}      ${bubble.positionPx.y}    ${bubble.color}")
                    bubblesToAdd.bubbles.add(bubble)
                    bubblesToRemove.add(bubblesSet.bubbles, position)
                }
            }
        }

        bubblesSet.bubbles.addAll(bubblesToAdd.bubbles)
        bubblesSet.bubbles.removeAll(bubblesToRemove.bubbles)
        return isBombUsed
    }

    fun usePhotoPart(point: Position): Boolean
    {
        var isUsed = false
        val bubblesToAdd = BubblesSet()
        val bubblesToRemove = BubblesSet()

        for(x in point.x until point.x + 24 step 8)
        {
            for (y in point.y until point.y + 24 step 8)
            {
                val position = Position(x, y)
                if (bubblesToRemove.add(bubblesSet.bubbles, position))
                {
                    isUsed = true

                    val pixels = selectPartOfOriginalImage(x, y, 8)
                    val bubble = createImageBubble(Position(x, y), pixels, false)
                    bubblesToAdd.bubbles.add(bubble)
                }
            }
        }

        for (x in point.x until point.x + 24)
        {
            for (y in point.y until point.y + 24)
            {
                bubblesToRemove.add(bubblesSet.bubbles, Position(x, y))
                sizeMap[x][y] = 1
            }
        }

        bubblesSet.bubbles.addAll(bubblesToAdd.bubbles)
        bubblesSet.bubbles.removeAll(bubblesToRemove.bubbles)

        return isUsed
    }

    fun useSwipe(bigBubblePosition: Position, splits: Int): Int
    {
        var availableSplits = splits
        val bigBubbleSize = sizeMap[bigBubblePosition.x][bigBubblePosition.y]
        if (bigBubbleSize in 2..availableSplits)
        {
            // Splitted bubble is 2x smaller
            val smallerBubbleSize = bigBubbleSize / 2

            // Calculate start position of big bubble
            val bigBubbleStartPosition = PositionCalculator.matchStartPositionOfBubble(bigBubblePosition, bigBubbleSize)
            bubblesSet.remove(bigBubbleStartPosition)

            // Get colors of all smallest bubbles in big bubble
            val colorsOfSmallestBubbles = PixelsUtilities.selectPartOf2DArray(smallBubblesColors, bigBubbleStartPosition, bigBubbleSize)

            // Create 4 smaller bubbles
            for (x in 0..1)
            {
                for (y in 0..1)
                {
                    val smallerBubblePoint = Position(x * smallerBubbleSize, y * smallerBubbleSize)
                    val partPixels = PixelsUtilities.selectPartOf2DArray(colorsOfSmallestBubbles, smallerBubblePoint, smallerBubbleSize)
                    val partSizePx = smallerBubbleSize * smallBubbleSizePx
                    val position = Position(bigBubbleStartPosition.x + x * smallerBubbleSize, bigBubbleStartPosition.y + y * smallerBubbleSize)
                    bubblesSet.bubbles.add(createColorBubble(position, partPixels, true, partSizePx))
                }
            }

            for (x in bigBubbleStartPosition.x until bigBubbleStartPosition.x + bigBubbleSize)
            {
                for (y in bigBubbleStartPosition.y until bigBubbleStartPosition.y + bigBubbleSize)
                    sizeMap[x][y] = smallerBubbleSize
            }

            availableSplits -= bigBubbleSize
        }
        return availableSplits
    }

    fun getSurfaceSize() = smallBubbleSizePx * IMAGE_SIDE

    fun convertPositionPxToBubblePosition(x: Float, y: Float) = Position(x.toInt() / smallBubbleSizePx, y.toInt() / smallBubbleSizePx)
}