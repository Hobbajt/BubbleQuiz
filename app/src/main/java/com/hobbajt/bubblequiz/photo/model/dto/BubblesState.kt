package com.hobbajt.bubblequiz.photo.model.dto

import com.hobbajt.bubblequiz.photo.model.BubblesSet
import java.io.Serializable
import java.util.*

data class BubblesState(val sizeMap : Array<IntArray>, val bubblesSet : BubblesSet): Serializable
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BubblesState

        if (!Arrays.deepEquals(sizeMap, other.sizeMap)) return false
        if (bubblesSet != other.bubblesSet) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = Arrays.deepHashCode(sizeMap)
        result = 31 * result + bubblesSet.hashCode()
        return result
    }
}
