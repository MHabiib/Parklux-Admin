package com.future.pms.admin.util

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.ScrollView
import com.future.pms.admin.R

class CustomZoomScrollView : ScrollView {

  // step 1: add some instance
  private var mScale = 1f
  private lateinit var mScaleDetector: ScaleGestureDetector
  private lateinit var gestureDetector: GestureDetector

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    //step 2: create instance from GestureDetector(this step should be place into onCreate())
    gestureDetector = GestureDetector(getContext(), GestureListener())

    mScaleDetector = ScaleGestureDetector(getContext(),
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
          override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = 1 - detector.scaleFactor

            val prevScale = mScale
            mScale += scale

            if (mScale < 0.1f)
            // Minimum scale condition:
              mScale = 0.1f

            if (mScale > 10f)
            // Maximum scale condition:
              mScale = 10f
            val scaleAnimation = ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale,
                1f / mScale, detector.focusX, detector.focusY)
            scaleAnimation.duration = 0
            scaleAnimation.fillAfter = true
            rootView.findViewById<View>(R.id.scroll_view).startAnimation(scaleAnimation)
            return true
          }
        })
  }

  // step 3: override dispatchTouchEvent()
  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    super.dispatchTouchEvent(event)
    mScaleDetector.onTouchEvent(event)
    gestureDetector.onTouchEvent(event)
    return gestureDetector.onTouchEvent(event)
  }

  //step 4: add private class GestureListener

  private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
    override fun onDown(e: MotionEvent): Boolean {
      return true
    }

    // event when double tap occurs
    override fun onDoubleTap(e: MotionEvent): Boolean {
      // double tap fired.
      return true
    }
  }
}