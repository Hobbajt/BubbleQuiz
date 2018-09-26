package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.photo.model.dto.Bubble
import com.hobbajt.bubblequiz.photo.model.dto.Position
import java.io.Serializable
import java.util.concurrent.CopyOnWriteArraySet

class BubblesSet : CopyOnWriteArraySet<Bubble>(), Serializable
{
    fun remove(position: Position)
    {
        val bubble = get(position)
        if(bubble != null && bubble.isChangeable)
        {
            remove(bubble)
        }
    }

    fun isChangeable(position: Position): Boolean = get(position)?.isChangeable ?: true

    fun add(items: CopyOnWriteArraySet<Bubble>, positionPx: Position): Boolean
    {
        for (drawItem in items)
        {
            if (drawItem.isChangeable && drawItem.position.x == positionPx.x && drawItem.position.y == positionPx.y)
            {
                add(drawItem)
                return true
            }
        }
        return false
    }

    fun get(position: Position): Bubble? = firstOrNull { it.position.x == position.x && it.position.y == position.y }
}