package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.photo.model.dto.Bubble
import com.hobbajt.bubblequiz.photo.model.dto.Position
import java.io.Serializable
import java.util.concurrent.CopyOnWriteArraySet

class BubblesSet : Serializable
{
    val bubbles = CopyOnWriteArraySet<Bubble>()

    fun remove(position: Position)
    {
        val bubble = get(position)
        if(bubble != null && bubble.isChangeable)
        {
            bubbles.remove(bubble)
        }
    }

    fun isChangeable(position: Position): Boolean = get(position)?.isChangeable ?: true

    fun add(items: Collection<Bubble>, positionPx: Position): Boolean
    {
        for (drawItem in items)
        {
            if (drawItem.isChangeable && drawItem.position == positionPx)
            {
                bubbles.add(drawItem)
                return true
            }
        }
        return false
    }

    fun get(position: Position): Bubble? = bubbles.firstOrNull { it.position == position}
}