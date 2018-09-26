package com.hobbajt.bubblequiz.photo.model.dto

import java.io.Serializable

/**
 * @param position         Position of center of circle
 * @param positionPx       Position of center of circle expressed in px
 */
class ColorBubble(val color : Int,
                  val radiusPx: Int,
                  position: Position,
                  positionPx: Position,
                  isChangeable: Boolean): Bubble(position, positionPx, isChangeable), Serializable
