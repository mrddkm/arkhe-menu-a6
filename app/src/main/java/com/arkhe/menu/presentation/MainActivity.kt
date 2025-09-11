package com.arkhe.menu.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.navigation.ArkheNavigation
import com.arkhe.menu.presentation.theme.AppTheme
import org.koin.androidx.compose.KoinAndroidContext

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "🚀 MainActivity onCreate")

        setContent {
            var hasError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }

            KoinAndroidContext {
                AppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (hasError) {
                            ErrorScreen(
                                errorMessage = errorMessage,
                                onRetry = {
                                    hasError = false
                                    errorMessage = ""
                                    recreate() // Restart activity
                                }
                            )
                        } else {
                            ArkheNavigation()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "📱 MainActivity onStart")
        validateDependencies()
    }

    private fun validateDependencies() {
        try {
            val koin = org.koin.core.context.GlobalContext.getOrNull()
            if (koin == null) {
                Log.w(TAG, "⚠️ Koin context is null on activity start")
                return
            }

            // Quick validation of critical dependencies
            val categoryUseCases =
                koin.getOrNull<com.arkhe.menu.domain.usecase.category.CategoryUseCases>()
            if (categoryUseCases == null) {
                Log.w(TAG, "⚠️ CategoryUseCases is null - DI might need re-initialization")
            } else {
                Log.d(TAG, "✅ Dependencies validated successfully")
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Dependency validation failed: ${e.message}", e)
        }
    }
}

/**
 * Error screen to display when DI or other critical errors occur
 */
@Composable
private fun ErrorScreen(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "⚠️ Application Error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("🔄 Restart App")
            }
        }
    }
}