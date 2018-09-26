package com.hobbajt.bubblequiz.levelpacks.model.dto

import java.io.Serializable

class LevelsPack(val id: Int, val levelsCount: Int, val requiredPassedLevels: Int) : Serializable
{
    var passedLevels: Int = 0
}