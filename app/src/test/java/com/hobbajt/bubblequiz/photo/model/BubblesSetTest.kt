package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.photo.model.dto.ColorBubble
import com.hobbajt.bubblequiz.photo.model.dto.Position
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

class BubblesSetTest
{
    private var bubblesSet = BubblesSet()

    @Before
    fun setUp()
    {
        bubblesSet.bubbles.add(ColorBubble(0, 100, Position(5,7), Position(50, 70), true))
        bubblesSet.bubbles.add(ColorBubble(0, 100, Position(6,8), Position(60, 80), true))
        bubblesSet.bubbles.add(ColorBubble(0, 100, Position(2,3), Position(20, 30), false))
    }

    @Test
    fun getTest()
    {
        var bubbleAtPosition = bubblesSet.get(Position(5, 7))

        Assert.assertNotNull(bubbleAtPosition)
        bubbleAtPosition?.let {
            Assert.assertEquals(it.position.x, 5)
            Assert.assertEquals(it.position.y, 7)
        }

        bubbleAtPosition = bubblesSet.get(Position(1, 1))
        Assert.assertNull(bubbleAtPosition)
    }

    @Test
    fun getIfChangeableTest()
    {
        var bubbleAtPosition = bubblesSet.getIfChangeable(Position(5, 7))

        Assert.assertNotNull(bubbleAtPosition)
        bubbleAtPosition?.let {
            Assert.assertEquals(it.position.x, 5)
            Assert.assertEquals(it.position.y, 7)
        }

        bubbleAtPosition = bubblesSet.getIfChangeable(Position(1, 1))
        Assert.assertNull(bubbleAtPosition)

        bubbleAtPosition = bubblesSet.getIfChangeable(Position(2, 3))
        Assert.assertNull(bubbleAtPosition)
    }

    @Test
    fun removeTest()
    {
        val position = Position(6, 8)
        Assert.assertNotNull(bubblesSet.get(position))
        bubblesSet.remove(position)
        Assert.assertNull(bubblesSet.get(position))
    }
}