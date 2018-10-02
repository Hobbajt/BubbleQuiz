package com.hobbajt.bubblequiz.application

import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import com.hobbajt.bubblequiz.levels.model.dto.Level
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api
{
    @GET("getSets.php")
    fun getSets(): Observable<List<LevelsPack>>

    @GET("getPhoto.php?")
    fun getPhoto(@Query("setId") setId: Int, @Query("levelId") levelId: Int, @Query("language") language: String, @Query("size") screenWidth: Int): Single<Level>
}
