package com.aadya.whiskyapp.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

class onEventsPageSwipeUpListner internal constructor(ctx: Context, mainView: View) :
    View.OnTouchListener {
    private val gestureDetector: GestureDetector
    var context: Context
    lateinit var mainView : View
    private var eventpageliveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()


    fun getLiveData(): MutableLiveData<Boolean?> {
        return eventpageliveData
    }


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }


    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            onSwipeTop()
            return super.onDoubleTap(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100) {
                    if (diffY > 0) {
                        //onSwipeBottom()
                    } else {
                       // onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }


    }

    fun onSwipeRight() {
    }

    fun onSwipeLeft() {
    }

    fun onSwipeTop() {
        eventpageliveData.value = true
    }

    fun onSwipeBottom() {



    }




    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
        mainView.setOnTouchListener(this)
        context = ctx
        this.mainView = mainView
    }
}