package com.arkhe.menu.presentation.screen.auth.onboarding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.arkhe.menu.domain.model.ThemeModels

/**
 * OnBoarding UI – Reusable BottomSheet for Activation, Login, PIN and Settings Entry
 * Compatible with OnBoardingScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingUI(
    showSettings: Boolean,
    currentThemeModel: ThemeModels,
    onDismissAll: () -> Unit
) {
    if (showSettings) {
        OnBoardingSettingsBottomSheet(
            onDismiss = onDismissAll,
            currentThemeModel = currentThemeModel
        )
    }
}
