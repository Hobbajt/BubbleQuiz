package com.hobbajt.bubblequiz.photo.model

class AnswerChecker
{
    companion object
    {
        private const val SPECIAL_CHARACTERS = "żźąćęłóśń"
        private const val NORMAL_CHARACTERS = "zzacelosn"

        fun isAnswerCorrect(userAnswer: String, correctAnswer: String): Boolean
        {
            return formatAnswer(correctAnswer).equals(formatAnswer(userAnswer), true)
        }

        private fun formatAnswer(answer: String): String
        {
            var formattedAnswer = answer
            formattedAnswer = formattedAnswer.replace(" ", "").toLowerCase()
            for (i in 0 until SPECIAL_CHARACTERS.length)
                formattedAnswer = formattedAnswer.replace(SPECIAL_CHARACTERS[i], NORMAL_CHARACTERS[i])
            return formattedAnswer
        }
    }
}