package com.hobbajt.bubblequiz.photo.model

import com.hobbajt.bubblequiz.application.Api
import com.hobbajt.bubblequiz.levels.model.dto.Level
import io.reactivex.Single
import javax.inject.Inject

class PhotoApiLoader @Inject constructor(val api: Api)
{
    fun load(setId: Int, levelId: Int, language: String, size: Int): Single<Level> = api.getPhoto(setId, levelId, language, size)
}
