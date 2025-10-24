package com.arkhe.menu.presentation.screen.auth.onboarding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.presentation.screen.auth.activation.ActivationBottomSheet
import com.arkhe.menu.presentation.screen.auth.lockscreen.PinLockBottomSheet
import com.arkhe.menu.presentation.screen.auth.signin.SignInBottomSheet
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.SuccessType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * OnBoarding UI – Reusable BottomSheet for Activation, Login, PIN and Settings Entry
 * Compatible with OnBoardingScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingUI(
    showActivation: Boolean,
    showSignedIn: Boolean,
    showPin: Boolean,
    showSettings: Boolean,
    currentThemeModel: ThemeModels,
    onDismissAll: () -> Unit,
    onActivated: () -> Unit,
    onSignedIn: () -> Unit,
    onUnlocked: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                val success = uiState as AuthUiState.Success
                println("AuthUi: Success type = ${success.type}, message = ${success.message}")
                when (success.type) {
                    SuccessType.ACTIVATION -> {
                        if (success.message.contains("PIN saved", true)) {
                            onDismissAll()
                            onActivated()
                        }
                    }

                    SuccessType.SIGNEDIN -> {
                        onDismissAll()
                        onSignedIn()
                    }
                }
            }

            is AuthUiState.Failed -> {}
            else -> Unit
        }
    }

    if (showActivation) {
        ActivationBottomSheet(
            onDismiss = onDismissAll,
            onActivated = onActivated,
            authViewModel = viewModel
        )
    }

    if (showSignedIn) {
        SignInBottomSheet(
            onDismiss = onDismissAll,
            onSignedIn = { userId, password ->
                scope.launch { viewModel.signedIn(userId, password) }
            }
        )
    }

    if (showPin) {
        PinLockBottomSheet(
            onDismiss = onDismissAll,
            onPinEntered = { pin ->
                scope.launch {
                    val ok = viewModel.unlockWithPin(pin)
                    if (ok) {
                        viewModel.resetPinAttempts()
                        onUnlocked()
                        onDismissAll()
                    } else {
                        viewModel.incrementPinAttempts()
                        val attempt = viewModel.getPinAttempts()
                        if (attempt >= 3) {
                            viewModel.resetPinAttempts()
                            onDismissAll()
                        }
                    }
                }
            }
        )
    }

    if (showSettings) {
        OnBoardingSettingsBottomSheet(
            onDismiss = onDismissAll,
            currentThemeModel = currentThemeModel
        )
    }
}
