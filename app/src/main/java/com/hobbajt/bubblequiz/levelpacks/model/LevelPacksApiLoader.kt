package com.hobbajt.bubblequiz.levelpacks.model

import com.hobbajt.bubblequiz.application.Api
import com.hobbajt.bubblequiz.levelpacks.model.dto.LevelsPack
import io.reactivex.Observable
import javax.inject.Inject

class LevelPacksApiLoader @Inject constructor(private val api: Api)
{
    fun load(): Observable<List<LevelsPack>> = api.getSets()
}
