@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.ui.animation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

object ScreenTransitions {

    private const val ANIMATION_DURATION = 350
    private const val FADE_DURATION = 200

    /**
     * Slide transition dari kanan ke kiri (forward navigation)
     */
    fun slideFromRight(): ContentTransform {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(ANIMATION_DURATION, easing = EaseOut)
        ) + fadeIn(
            animationSpec = tween(FADE_DURATION)
        ) togetherWith slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(ANIMATION_DURATION, easing = EaseInOut)
        ) + fadeOut(
            animationSpec = tween(FADE_DURATION)
        )
    }

    /**
     * Slide transition dari kiri ke kanan (back navigation)
     */
    fun slideFromLeft(): ContentTransform {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(ANIMATION_DURATION, easing = EaseOut)
        ) + fadeIn(
            animationSpec = tween(FADE_DURATION)
        ) togetherWith slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(ANIMATION_DURATION, easing = EaseInOut)
        ) + fadeOut(
            animationSpec = tween(FADE_DURATION)
        )
    }

    /**
     * No animation untuk transisi yang tidak perlu animasi
     */
    fun noAnimation(): ContentTransform {
        return fadeIn(
            animationSpec = tween(0)
        ) togetherWith fadeOut(
            animationSpec = tween(0)
        )
    }

    /**
     * Fade transition untuk perubahan content yang subtle
     */
    fun crossFade(): ContentTransform {
        return fadeIn(
            animationSpec = tween(300, easing = EaseInOut)
        ) togetherWith fadeOut(
            animationSpec = tween(300, easing = EaseInOut)
        )
    }
}