package com.albertkhang.app.animations

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator


class RotationView {
    var view: View? = null
    private var duration: Long = 20000
    private var rotateAnimation: ObjectAnimator? = null

    fun start() {
        if (view != null) {
            Log.d("RotationView", "startRotationView")

            rotateAnimation = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f)
            rotateAnimation?.interpolator = LinearInterpolator()
            rotateAnimation?.repeatCount = Animation.INFINITE
            rotateAnimation?.duration = duration

            rotateAnimation?.start()
        }
    }

    fun resume() {
        rotateAnimation?.resume()
    }

    fun pause() {
        if (rotateAnimation != null) {
            Log.d("RotationView", "pauseRotationView")
            rotateAnimation?.pause()
        }
    }

    fun isNull(): Boolean {
        if (rotateAnimation == null) {
            return true
        }
        return false
    }
}

