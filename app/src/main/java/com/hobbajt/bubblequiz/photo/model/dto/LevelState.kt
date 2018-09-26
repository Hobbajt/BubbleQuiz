package com.hobbajt.bubblequiz.photo.model.dto

import java.io.Serializable

data class LevelState(var bubblesState: BubblesState?, val availableSplits : Int, val restartCount: Int) : Serializable
