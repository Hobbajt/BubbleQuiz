package com.hobbajt.bubblequiz.photo.view.customview.singleinputview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hobbajt.bubblequiz.R
import java.util.*

class InputViewContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        LinearLayout(context, attrs, defStyleAttr), InputViewLine.OnLineChangedListener
{
    private var listener: OnCompleteListener? = null

    private val lines = ArrayList<InputViewLine>()
    private val wordSignsCount = ArrayList<Int>()
    private var formattedAnswer = ""

    interface OnCompleteListener
    {
        fun onComplete(answer: String)
    }

    fun setAnswerPattern(answer: String)
    {
        if (answer.isNotBlank())
        {
            answer.trim()
                    .split(" ")
                    .flatMap { splitWord(it) }
                    .forEach { word ->
                        formattedAnswer += "$word "
                        wordSignsCount.add(word.length)
                        addLine(lines.size, word.length)
                    }
            lines[0].moveToBox(0)
        }
    }

    private fun splitWord(word: String): List<String>
    {
        var splittedWord = listOf(word)
        if (word.length >= 10)
            splittedWord = word.split("(?<=\\G.{5})")
        return splittedWord
    }

    private fun addLine(id: Int, signsCount: Int)
    {
        val view = LayoutInflater.from(context).inflate(R.layout.input_view_line, this)
        val line = view.findViewById<InputViewLine>(R.id.ivLine)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 16, 0, 0)
        line.layoutParams = layoutParams
        line.id = id
        line.tag = id
        line.setSignsCount(signsCount)
        line.setOnLineChangedListener(this)
        lines.add(line)
    }

    fun displayAnswer()
    {
        val words = formattedAnswer.trim().split(" ")

        for (i in lines.indices)
        {
            lines[i].displayWord(words[i])
        }
    }

    fun setColor(color: Int)
    {
        for (ivLine in lines)
        {
            ivLine.setColor(color)
        }
    }

    fun setOnCompleteListener(listener: OnCompleteListener)
    {
        this.listener = listener
    }

    override fun onLineFilled(lineId: Int, signs: String)
    {
        val answer = StringBuilder()
        for (ivLine in lines)
        {
            answer.append(ivLine.signs)
        }

        listener?.onComplete(answer.toString().trim())
    }

    override fun focusNextLine(lineId: Int)
    {
        if (lineId in 0 until lines.lastIndex)
        {
            lines[lineId + 1].moveToBox(0)
        }
    }

    override fun focusPreviousLine(lineId: Int)
    {
        if (lineId >= 1)
        {
            lines[lineId - 1].moveToBox(wordSignsCount[lineId - 1] - 1)
        }
    }
}