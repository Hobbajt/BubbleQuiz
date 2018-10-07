package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.photo.model.dto.Position
import junit.framework.Assert
import org.junit.Test

class PositionCalculatorTest
{
    @Test
    fun matchStartPositionOfBubble()
    {
        Assert.assertTrue(PositionCalculator.matchStartPositionOfBubble(Position(17, 42), 8) == Position(16, 40))
        Assert.assertTrue(PositionCalculator.matchStartPositionOfBubble(Position(16, 40), 8) == Position(16, 40))
        Assert.assertTrue(PositionCalculator.matchStartPositionOfBubble(Position(0, 0), 8) == Position(0, 0))
    }

    @Test
    fun matchStartPositionOfHintTest()
    {
        Assert.assertTrue(PositionCalculator.matchStartPositionOfHint(Position(9, 5)) == Position(0, 0))
        Assert.assertTrue(PositionCalculator.matchStartPositionOfHint(Position(4, 42)) == Position(0, 32))
        Assert.assertTrue(PositionCalculator.matchStartPositionOfHint(Position(25, 17)) == Position(16, 8))
    }
}