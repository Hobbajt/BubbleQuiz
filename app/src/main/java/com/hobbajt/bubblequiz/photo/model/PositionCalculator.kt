package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.photo.model.dto.Position
import org.apache.commons.lang3.math.NumberUtils

object PositionCalculator
{
    fun matchStartPositionOfHint(point: Position): Position
    {
        val bubbleStartPosition = matchStartPositionOfBubble(point, 8)
        var startX = NumberUtils.max(bubbleStartPosition.x - 8, 0)
        var startY = NumberUtils.max(bubbleStartPosition.y - 8, 0)
        startX = NumberUtils.min(startX, 40)
        startY = NumberUtils.min(startY, 40)
        return Position(startX, startY)
    }

    fun matchStartPositionOfBubble(point: Position, bubbleSize: Int): Position
    {
        val startX = point.x - point.x % bubbleSize
        val startY = point.y - point.y % bubbleSize
        return Position(startX, startY)
    }
}