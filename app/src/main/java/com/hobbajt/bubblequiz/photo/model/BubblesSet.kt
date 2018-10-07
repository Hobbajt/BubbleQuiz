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

    fun get(position: Position): Bubble? = bubbles.firstOrNull { it.position == position}

    fun getIfChangeable(position: Position): Bubble?
    {
        val bubble = get(position)
        if(bubble?.isChangeable == true)
            return bubble
        return null

    }

}