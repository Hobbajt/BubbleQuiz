package com.hobbajt.bubblequiz.photo.model.dto

import com.hobbajt.bubblequiz.photo.model.BubblesSet
import java.io.Serializable
import java.util.*

data class BubblesState(val sizeMap : Array<IntArray>, val bubblesSet : BubblesSet): Serializable