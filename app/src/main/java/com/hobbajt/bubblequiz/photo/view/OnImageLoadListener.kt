package com.hobbajt.bubblequiz.photo.view

interface OnImageLoadListener
{
    fun onSuccess(imageBytes: ByteArray)

    fun onFailed(exception: Throwable)
}