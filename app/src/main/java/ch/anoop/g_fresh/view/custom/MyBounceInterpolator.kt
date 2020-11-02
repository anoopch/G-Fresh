package ch.anoop.g_fresh.view.custom

import android.view.animation.Interpolator
import kotlin.math.cos
import kotlin.math.pow

/**
 * Animation class for the Fav button animation effect.
 *      -- Combined with R.anim.fav_view_bounce for smoothness
 */
class MyBounceInterpolator(private val amp: Double, private val freq: Double) : Interpolator {

    override fun getInterpolation(time: Float) =
        (-1 * Math.E.pow(-time / amp) * cos(freq * time) + 1).toFloat()
}