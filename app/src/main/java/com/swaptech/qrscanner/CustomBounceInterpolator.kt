package com.swaptech.qrscanner


import android.view.animation.Interpolator
import kotlin.math.cos

internal class CustomBounceInterpolator(private val amplitude: Double, private val frequency: Double): android.view.animation.Interpolator {

    override fun getInterpolation(time: Float): Float {
        return (-1 * Math.pow(Math.E, -time / amplitude) *
                Math.cos(frequency * time) + 1).toFloat()
    }
}

