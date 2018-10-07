package com.hobbajt.bubblequiz.sharedprefs

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.hobbajt.bubblequiz.levels.model.dto.Level
import com.nhaarman.mockitokotlin2.spy
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class LocalDataEditorTest
{
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var localDataEditor: LocalDataEditor

    @Before
    fun setUp()
    {
        val context = RuntimeEnvironment.application.applicationContext
        sharedPreferences = spy(PreferenceManager.getDefaultSharedPreferences(context))
        localDataEditor = LocalDataEditor(sharedPreferences, Gson())
        localDataEditor.clear()
    }

    @Test
    fun clearTest()
    {
        sharedPreferences.edit().putString("test", "abc").apply()
        assertEquals(sharedPreferences.getString("test", ""), "abc")
        localDataEditor.clear()
        assertNull(sharedPreferences.getString("test", null))
    }

    @Test
    fun isLevelPassedTest()
    {
        val level = Level(5, 2, "url", "answer")
        assertFalse(localDataEditor.isLevelPassed(level))
        localDataEditor.writePassedLevel(level)
        assertTrue(localDataEditor.isLevelPassed(level))
    }

    @Test
    fun readPassedLevelsInPackTest()
    {
        assertTrue(localDataEditor.readPassedLevelsInPack(1).isEmpty())
        var level = Level(5, 5, "url", "answer")

        localDataEditor.writePassedLevel(level)
        assertTrue(localDataEditor.readPassedLevelsInPack(1).isEmpty())

        level = Level(5, 1, "url", "answer")
        localDataEditor.writePassedLevel(level)
        assertTrue(localDataEditor.readPassedLevelsInPack(1).size == 1)
        assertTrue(localDataEditor.readPassedLevelsInPack(1).contains(level.id))

        level = Level(7, 1, "url", "answer2")
        localDataEditor.writePassedLevel(level)
        assertTrue(localDataEditor.readPassedLevelsInPack(1).size == 2)
        assertTrue(localDataEditor.readPassedLevelsInPack(1).contains(level.id))
    }

    @Test
    fun readTotalPassedLevelsCountTest()
    {
        val passedLevels = localDataEditor.readTotalPassedLevelsCount()

        var level = Level(5, 1, "url", "answer")
        localDataEditor.writePassedLevel(level)
        assertSame(passedLevels + 1, localDataEditor.readTotalPassedLevelsCount())

        level = Level(8, 1, "url", "answer")
        localDataEditor.writePassedLevel(level)
        assertSame(passedLevels + 2, localDataEditor.readTotalPassedLevelsCount())
    }

    @Test
    fun readPointsCountTest()
    {
        assertSame(localDataEditor.readPointsCount(), 0)
        localDataEditor.writePointsCount(5)
        assertSame(localDataEditor.readPointsCount(), 5)
        localDataEditor.writePointsCount(-2)
        assertSame(localDataEditor.readPointsCount(), 3)
    }
}