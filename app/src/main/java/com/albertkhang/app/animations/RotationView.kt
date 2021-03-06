package com.albertkhang.app.animations

import android.animation.ObjectAnimator
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator


class RotationView {
    var view: View? = null
    private var duration: Long = 20000
    private var rotateAnimation: ObjectAnimator? = null
    var fractionValue: Float? = null

    fun getDefault() {
        if (view != null) {
            Log.d("RotationView", "getDefault()RotationView")
            rotateAnimation = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f)
            rotateAnimation?.interpolator = LinearInterpolator()
            rotateAnimation?.repeatCount = Animation.INFINITE
            rotateAnimation?.duration = duration
        }
    }

    fun resetAnimator() {
        Log.d("RotationView", "resetAnimator")
        rotateAnimation = null
    }

    fun getAnimatedFraction(): Float? {
        return rotateAnimation?.animatedFraction
    }

    fun setAnimatedFraction(float: Float?) {
        if (float != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                rotateAnimation?.setCurrentFraction(float)
            }
        }
    }

    fun start() {
        if (view != null) {
            Log.d("RotationView", "startRotationView")
            rotateAnimation?.start()
        }
    }

    fun resume() {
        if (rotateAnimation != null) {
            Log.d("RotationView", "resumeRotationView")
            rotateAnimation?.resume()
        }
    }

    fun pause() {
        if (rotateAnimation != null) {
            Log.d("RotationView", "pauseRotationView")
            rotateAnimation?.pause()
        }
    }

    fun end() {
        if (rotateAnimation != null) {
            Log.d("RotationView", "endRotationView")
            rotateAnimation?.end()
        }
    }

    fun isNull(): Boolean {
        if (rotateAnimation == null) {
            Log.d("RotationView", "isNull()RotationView")
            return true
        }
        return false
    }
}

