package com.hobbajt.bubblequiz.photo.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.hobbajt.bubblequiz.photo.model.dto.Bubble
import com.hobbajt.bubblequiz.photo.model.dto.ColorBubble
import com.hobbajt.bubblequiz.photo.model.dto.ImageBubble
import java.util.concurrent.CopyOnWriteArraySet

class PhotoSurfaceView : SurfaceView, SurfaceHolder.Callback, Runnable
{
    private var view: PhotoContractor.View? = null

    private var surfaceSize = 0
    private val paint = Paint()
    private var drawThread: Thread = Thread(this)
    private lateinit var surfaceHolder: SurfaceHolder
    private var bubbles: CopyOnWriteArraySet<Bubble> = CopyOnWriteArraySet()
    private var isRunning = false

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    private fun init()
    {
        keepScreenOn = true
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
    }

    fun bind(bubbles: CopyOnWriteArraySet<Bubble>, surfaceSize: Int, listener: OnTouchListener)
    {
        this.bubbles = bubbles
        this.surfaceSize = surfaceSize
        setOnTouchListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    fun setView(view: PhotoContractor.View)
    {
        this.view = view
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder)
    {
        isRunning = true
        if (drawThread.state  == Thread.State.NEW)
        {
            drawThread.start()
        }
        else if(drawThread.state == Thread.State.TERMINATED)
        {
            drawThread = Thread(this)
            drawThread.start()
        }

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder)
    {
        isRunning = false
    }

    override fun run()
    {
        while (isRunning)
        {
            val bitmap = Bitmap.createBitmap(surfaceSize, surfaceSize, Bitmap.Config.ARGB_8888)
            val offCanvas = Canvas(bitmap)
            for (bubble in bubbles)
            {
                when(bubble)
                {
                    is ColorBubble ->
                    {
                        paint.color = bubble.color
                        offCanvas.drawCircle(bubble.positionPx.x.toFloat() + bubble.radiusPx.toFloat(),
                                             bubble.positionPx.y.toFloat() + bubble.radiusPx.toFloat(), bubble.radiusPx.toFloat(), paint)
                    }
                    is ImageBubble ->
                    {
                        val size = Math.sqrt(bubble.pixels.size.toDouble())
                        val bubbleBitmap = Bitmap.createBitmap(bubble.pixels, size.toInt(), size.toInt(), Bitmap.Config.ARGB_8888)
                        offCanvas.drawBitmap(bubbleBitmap, bubble.positionPx.x.toFloat(), bubble.positionPx.y.toFloat(), paint)
                    }
                }
            }
            draw(bitmap)
        }
    }

    private fun draw(bitmap: Bitmap)
    {
        try
        {
            val canvas = surfaceHolder.lockCanvas(null)
            if (canvas != null)
            {
                canvas.drawColor(0, PorterDuff.Mode.CLEAR)
                canvas.drawBitmap(bitmap, 0f, 0f, paint)
            }

            surfaceHolder.unlockCanvasAndPost(canvas)

        }
        catch (e: Exception)
        {
            isRunning = false
        }
    }
}