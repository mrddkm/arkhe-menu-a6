package com.arkhe.menu.presentation.screen.auth

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.arkhe.menu.presentation.screen.auth.activation.ActivationBottomSheet
import com.arkhe.menu.presentation.screen.auth.lockscreen.PinPadBottomSheet
import com.arkhe.menu.presentation.screen.auth.signin.SignInBottomSheet
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.SuccessType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * AUTH UI – Reusable BottomSheet for Activation, Login, and PIN Entry
 * Compatible with OnBoardingScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthUi(
    showActivation: Boolean,
    showSignedIn: Boolean,
    showPin: Boolean,
    onDismissAll: () -> Unit,
    onActivated: () -> Unit,
    onSignedIn: () -> Unit,
    onUnlocked: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                Toast.makeText(
                    context,
                    (uiState as AuthUiState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()

                onDismissAll()

                when ((uiState as AuthUiState.Success).type) {
                    SuccessType.ACTIVATION -> onActivated()
                    SuccessType.SIGNEDIN -> onSignedIn()
                }
            }

            is AuthUiState.Error -> {
                Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

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
            onSubmit = { userId, password ->
                scope.launch { viewModel.signIn(userId, password) }
            }
        )
    }

    if (showPin) {
        PinPadBottomSheet(
            onDismiss = onDismissAll,
            onPinEntered = { pin ->
                scope.launch {
                    val ok = viewModel.unlockWithPin(pin)
                    if (ok) {
                        Toast.makeText(context, "PIN correct", Toast.LENGTH_SHORT).show()
                        viewModel.resetPinAttempts()
                        onUnlocked()
                    } else {
                        viewModel.incrementPinAttempts()
                        val attempt = viewModel.getPinAttempts()
                        if (attempt >= 3) {
                            Toast.makeText(
                                context,
                                "Too many attempts. Try again later.",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.resetPinAttempts()
                            onDismissAll()
                        } else {
                            Toast.makeText(context, "Wrong PIN ($attempt/3)", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        )
    }
}
