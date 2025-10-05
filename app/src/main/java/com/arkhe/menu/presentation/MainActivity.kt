package com.arkhe.menu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkhe.menu.presentation.navigation.ArkheNavigation
import com.arkhe.menu.presentation.ui.splash.SplashScreen
import com.arkhe.menu.presentation.ui.theme.AppTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.KoinAndroidContext

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            KoinAndroidContext {
                AppTheme {
                    var showCustomSplash by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(500)
                        keepSplashOnScreen = false

                        delay(2500)
                        showCustomSplash = false
                    }

                    if (showCustomSplash) {
                        SplashScreen()
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ArkheNavigation()
                        }
                    }
                }
            }
        }
    }
}