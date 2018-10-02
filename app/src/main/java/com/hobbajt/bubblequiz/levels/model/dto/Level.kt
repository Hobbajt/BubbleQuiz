package com.hobbajt.bubblequiz.levels.model.dto

import java.io.Serializable

data class Level(val id: Int, val setId: Int, val photoUrl: String, val answer: String) : Serializable
