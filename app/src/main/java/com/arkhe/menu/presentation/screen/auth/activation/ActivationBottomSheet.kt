package com.arkhe.menu.presentation.screen.auth.activation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.SuccessType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationBottomSheet(
    onDismiss: () -> Unit,
    onActivated: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state = rememberActivationState()
    val uiState by viewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { /* hanya dari tombol Cancel */ }
    )
    {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(uiState) {
                when (uiState) {
                    is AuthUiState.Success -> {
                        val success = uiState as AuthUiState.Success

                        when (success.type) {
                            SuccessType.ACTIVATION -> {
                                when {
                                    success.message.contains(
                                        "Activation request",
                                        true
                                    ) -> state.onStepChange(2)

                                    success.message.contains(
                                        "Code verified",
                                        true
                                    ) -> state.onStepChange(3)

                                    success.message.contains(
                                        "Password",
                                        true
                                    ) -> state.onStepChange(4)
                                }
                            }

                            else -> Unit
                        }
                    }

                    is AuthUiState.Error -> {
                        Toast.makeText(
                            state.context,
                            (uiState as AuthUiState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
            when (state.step) {
                1 -> ActivationContentStepOne(
                    state = state,
                    onNext = {
                        state.scope.launch {
                            viewModel.requestActivation(
                                state.userId,
                                state.phone,
                                state.email
                            )
                        }
                    }
                )

                2 -> ActivationContentStepTwo(
                    state = state,
                    onVerify = {
                        state.scope.launch {
                            viewModel.verifyActivationCode(
                                state.code
                            )
                        }
                    }
                )

                3 -> ActivationContentStepThree(
                    state = state,
                    onContinue = {
                        state.scope.launch {
                            if (state.password == state.confirmPassword) {
                                viewModel.createPassword(state.password)
                            } else {
                                Toast.makeText(
                                    state.context,
                                    "Password mismatch",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )

                4 -> ActivationContentStepFour(
                    state = state,
                    onFinish = {
                        state.scope.launch {
                            if (state.pin == state.confirmPin && state.pin.length == 4) {
                                viewModel.savePin(state.pin)
                                Toast.makeText(
                                    state.context,
                                    "Activation Complete!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onActivated()
                            } else {
                                Toast.makeText(state.context, "PIN mismatch", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                )
            }

            Spacer(Modifier.height(8.dp))
            if (state.step > 1) {
                TextButton(onClick = { state.onStepChange(state.step - 1) }) {
                    Text("Back")
                }
            }
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    }
}

@Composable
fun rememberActivationState(): ActivationState {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var step by remember { mutableIntStateOf(1) }
    var userId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    return ActivationState(
        step = step, onStepChange = { step = it },
        userId = userId, onUserIdChange = { userId = it },
        phone = phone, onPhoneChange = { phone = it },
        email = email, onEmailChange = { email = it },
        code = code, onCodeChange = { code = it },
        password = password, onPasswordChange = { password = it },
        confirmPassword = confirmPassword, onConfirmPasswordChange = { confirmPassword = it },
        pin = pin, onPinChange = { pin = it },
        confirmPin = confirmPin, onConfirmPinChange = { confirmPin = it },
        scope = scope, context = context
    )
}

data class ActivationState(
    val step: Int,
    val onStepChange: (Int) -> Unit,

    val userId: String,
    val onUserIdChange: (String) -> Unit,
    val phone: String,
    val onPhoneChange: (String) -> Unit,
    val email: String,
    val onEmailChange: (String) -> Unit,

    val code: String,
    val onCodeChange: (String) -> Unit,

    val password: String,
    val onPasswordChange: (String) -> Unit,
    val confirmPassword: String,
    val onConfirmPasswordChange: (String) -> Unit,

    val pin: String,
    val onPinChange: (String) -> Unit,
    val confirmPin: String,
    val onConfirmPinChange: (String) -> Unit,

    val scope: CoroutineScope,
    val context: Context
)
