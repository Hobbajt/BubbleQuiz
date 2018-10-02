package com.hobbajt.bubblequiz.photo.model

import java.text.Normalizer
import java.util.regex.Pattern


object AnswerChecker
{
    fun isAnswerCorrect(userAnswer: String, correctAnswer: String): Boolean
    {
        return formatAnswer(correctAnswer).toLowerCase() == formatAnswer(userAnswer).toLowerCase()
    }

    private fun formatAnswer(answer: String): String
    {
        var formattedAnswer = answer
        formattedAnswer = formattedAnswer.replace(" ", "").toLowerCase()

        val output = Normalizer.normalize(formattedAnswer, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")

        return pattern.matcher(output).replaceAll("")
    }
}