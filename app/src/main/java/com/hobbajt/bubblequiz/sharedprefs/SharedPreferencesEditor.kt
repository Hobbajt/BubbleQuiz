package com.hobbajt.bubblequiz.sharedprefs

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hobbajt.bubblequiz.levels.model.dto.Level
import org.apache.commons.lang3.ObjectUtils
import java.util.*
import javax.inject.Inject

class SharedPreferencesEditor @Inject constructor(private val sharedPreferences: SharedPreferences, private val gson: Gson)
{
    companion object
    {
        private const val POINTS_COUNT = "pointsCount"
        private const val PASSED_LEVELS = "passedLevels"
        private const val PASSED_LEVELS_COUNT = "passedLevelsCount"
    }

    fun clear()
    {
        sharedPreferences.edit().clear().apply()
    }

    fun isLevelPassed(level: Level): Boolean
    {
        val passedLevels = readPassedLevelsInPack(level.setID)
        return passedLevels.contains(level.levelID)
    }

    fun readPassedLevelsInPack(setId: Int): Set<Int>
    {
        val passedLevelsJson = sharedPreferences.getString(PASSED_LEVELS + setId, "")

        val type = object : TypeToken<Set<Int>>() {}.type
        val passedLevels = gson.fromJson<Set<Int>>(passedLevelsJson, type)

        return ObjectUtils.defaultIfNull<Set<Int>>(passedLevels, HashSet())
    }

    fun writePassedLevel(level: Level)
    {
        val levelsPackId = level.setID
        val passedLevels: MutableSet<Int> = readPassedLevelsInPack(levelsPackId).toMutableSet()

        if (!passedLevels.contains(level.levelID))
            writePassedLevelsCountIncrement()

        passedLevels.add(level.levelID)
        sharedPreferences.edit().putString(PASSED_LEVELS + levelsPackId, gson.toJson(passedLevels)).apply()
    }


    private fun writePassedLevelsCountIncrement()
    {
        var count = readTotalPassedLevelsCount()
        sharedPreferences.edit().putInt(PASSED_LEVELS_COUNT, ++count).apply()
    }

    fun readTotalPassedLevelsCount(): Int
    {
        return sharedPreferences.getInt(PASSED_LEVELS_COUNT, 0)
    }

    // region Points Count
    fun readPointsCount() = sharedPreferences.getInt(POINTS_COUNT, 0)

    fun writePointsCount(pointsCount: Int): Int
    {
        val totalCount = readPointsCount() + pointsCount
        sharedPreferences.edit().putInt(POINTS_COUNT, totalCount).apply()
        return totalCount
    }
    // endregion
}