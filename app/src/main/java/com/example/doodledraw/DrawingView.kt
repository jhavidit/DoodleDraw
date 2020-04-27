package com.example.doodledraw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs:AttributeSet): View(context,attrs) {
    private var mDrawPath:CustomPath?=null
    private var mDrawPaint:Paint?=null
    private var mCanvasBitmap:Bitmap?=null
    private var mBrushSize:Float=0.toFloat()
    private var color=Color.BLACK
    private var canvas:Canvas?=null
    private var mCanvasPaint:Paint?=null
    private var mPath=ArrayList<CustomPath>()



    init {
        setUpDrawing()
    }
    private fun setUpDrawing()
    {
        mDrawPaint= Paint()
        mDrawPath=CustomPath(color,mBrushSize)
        mBrushSize=20.toFloat()
        mDrawPaint!!.color=color
        mDrawPaint!!.style=Paint.Style.STROKE
        mDrawPaint!!.strokeJoin=Paint.Join.ROUND
        mDrawPaint!!.strokeCap=Paint.Cap.ROUND
        mCanvasPaint= Paint(Paint.DITHER_FLAG)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ALPHA_8)
        canvas= Canvas(mCanvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in mPath){
            mDrawPaint!!.strokeWidth=path.brushSize
            mDrawPaint!!.color=path.color
            canvas.drawPath(path!!, mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth=mDrawPath!!.brushSize
            mDrawPaint!!.color=mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX=event?.x
        val touchY=event?.y

        when(event?.action)
        {
            MotionEvent.ACTION_DOWN->{
                mDrawPath!!.color=color
                mDrawPath!!.brushSize=mBrushSize

               mDrawPath!!.reset()

                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE->
            {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP->
            {
                mPath.add(mDrawPath!!)
                mDrawPath=CustomPath(color,mBrushSize)
            }
            else->return false
        }

        invalidate()

        return true
    }
    internal inner class CustomPath(var color:Int,var brushSize:Float): Path() {

    }
}