package com.hobbajt.bubblequiz.levels.model.dto

import java.io.Serializable

data class Level(val levelID: Int, val setID: Int, val photoUrl: String, val answer: String) : Serializable
