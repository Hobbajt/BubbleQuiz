package com.hobbajt.bubblequiz.photo.model.dto

import java.io.Serializable

abstract class Bubble(val position: Position,
                      val positionPx: Position,
                      val isChangeable: Boolean) : Serializable
