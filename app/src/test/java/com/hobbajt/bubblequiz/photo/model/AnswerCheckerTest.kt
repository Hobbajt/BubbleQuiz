package com.hobbajt.bubblequiz.photo.model

import junit.framework.Assert
import org.junit.Test

class AnswerCheckerTest
{
    @Test
    fun isAnswerCorrectTest()
    {
        Assert.assertTrue(AnswerChecker.isAnswerCorrect("", ""))
        Assert.assertTrue(AnswerChecker.isAnswerCorrect("aBcdE", "abCDe"))
        Assert.assertTrue(AnswerChecker.isAnswerCorrect("ąBcłdE", "abCŁDę"))
    }
}