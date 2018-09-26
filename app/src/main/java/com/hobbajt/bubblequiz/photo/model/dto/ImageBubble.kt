package com.hobbajt.bubblequiz.photo.model.dto

import java.io.Serializable

/**
 * @param position         Position of upper left corner of pixels
 * @param positionPx       Position of upper left corner of pixels expressed in px
 */
class ImageBubble(val pixels: IntArray,
                  position: Position,
                  positionPx: Position,
                  isChangeable: Boolean): Bubble(position, positionPx, isChangeable), Serializable