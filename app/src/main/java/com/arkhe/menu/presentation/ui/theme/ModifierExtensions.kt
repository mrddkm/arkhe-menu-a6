package com.arkhe.menu.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Menyesuaikan padding otomatis berdasarkan jenis layout dan versi Android.
 *
 * @param hasBars Jika true, berarti screen memiliki TopBar/BottomBar (mis. MainScreen).
 *                Jika false, berarti screen fullscreen (mis. OnBoarding, Splash).
 */
@Suppress("UnnecessaryComposedModifier")
fun Modifier.autoInsets(hasBars: Boolean): Modifier = this.then(
    Modifier.composed(
        inspectorInfo = debugInspectorInfo {
            name = "autoInsets"
            value = "hasBars=$hasBars"
        }
    ) {
        when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.R -> {
                if (hasBars) {
                    Modifier.windowInsetsPadding(WindowInsets.systemBars)
                } else {
                    Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                }
            }

            else -> {
                Modifier
            }
        }
    }
)