package com.arkhe.menu.presentation.screen.auth.activation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.SuccessType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationBottomSheet(
    onDismiss: () -> Unit,
    onActivated: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
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

    val uiState by viewModel.uiState.collectAsState()

    ModalBottomSheet(onDismissRequest = { /* hanya dari tombol Cancel */ }) {
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
                                    success.message.contains("Activation request", true) -> step = 2
                                    success.message.contains("Code verified", true) -> step = 3
                                    success.message.contains("Password", true) -> step = 4
                                }
                            }

                            else -> Unit
                        }
                    }

                    is AuthUiState.Error -> {
                        Toast.makeText(
                            context,
                            (uiState as AuthUiState.Error).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
            when (step) {
                /*1️⃣ STEP 1 — USER DATA*/
                1 -> {
                    Text("Activation - Step 1 of 4", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("User ID") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.requestActivation(userId, phone, email)
                                step = 2
                            }
                        },
                        enabled = userId.isNotBlank() && phone.isNotBlank() && email.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }

                /*2️⃣ STEP 2 — CODE VERIFICATION*/
                2 -> {
                    Text(
                        "Enter 4-digit Activation Code",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = code,
                        onValueChange = { if (it.length <= 4) code = it },
                        label = { Text("Activation Code") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.verifyActivationCode(code)
                            }
                        },
                        enabled = code.length == 4,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify")
                    }
                }

                // 3️⃣ STEP 3 — CREATE PASSWORD
                3 -> {
                    Text("Create New Password", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                if (password == confirmPassword) {
                                    viewModel.createPassword(password)
                                } else {
                                    Toast.makeText(context, "Password mismatch", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        },
                        enabled = password.isNotBlank() && confirmPassword == password,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                }

                // 4️⃣ STEP 4 — CREATE PIN
                4 -> {
                    Text("Create 4-digit PIN", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4) pin = it },
                        label = { Text("New PIN") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { if (it.length <= 4) confirmPin = it },
                        label = { Text("Confirm PIN") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                if (pin == confirmPin && pin.length == 4) {
                                    viewModel.savePin(pin)
                                    Toast.makeText(
                                        context,
                                        "Activation Complete!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onActivated() // ✅ callback to OnBoardingScreen
                                } else {
                                    Toast.makeText(context, "PIN mismatch", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        },
                        enabled = pin.length == 4 && confirmPin.length == 4,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finish")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            if (step > 1) {
                TextButton(onClick = { step-- }) {
                    Text("Back")
                }
            }
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    }
}
