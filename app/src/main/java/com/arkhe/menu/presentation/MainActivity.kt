package com.arkhe.menu.presentation

import com.arkhe.menu.presentation.theme.ArkheTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkhe.menu.presentation.navigation.TripkeunNavigation
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                ArkheTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        TripkeunNavigation()
                    }
                }
            }
        }
    }
}