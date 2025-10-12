package com.arkhe.menu.presentation.screen.auth.activation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.ArkheTheme

@Composable
fun ActivationContentStepOne(
    state: ActivationState,
    onNext: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Activation - Step 1 of 4", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.userId,
            onValueChange = state.onUserIdChange,
            label = { Text("User ID") },
            singleLine = true
        )
        OutlinedTextField(
            value = state.phone,
            onValueChange = state.onPhoneChange,
            label = { Text("Phone Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = state.onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onNext,
            enabled = state.userId.isNotBlank() && state.phone.isNotBlank() && state.email.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun ActivationContentStepTwo(
    state: ActivationState,
    onVerify: () -> Unit
) {
    Text(
        "Enter 4-digit Activation Code",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(
        value = state.code,
        onValueChange = { if (it.length <= 4) state.onCodeChange },
        label = { Text("Activation Code") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = onVerify,
        enabled = state.code.length == 4,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Verify")
    }
}

@Composable
fun ActivationContentStepThree(
    state: ActivationState,
    onContinue: () -> Unit
) {
    Text("Create New Password", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(
        value = state.password,
        onValueChange = state.onPasswordChange,
        label = { Text("New Password") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    OutlinedTextField(
        value = state.confirmPassword,
        onValueChange = state.onConfirmPasswordChange,
        label = { Text("Confirm Password") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = onContinue,
        enabled = state.password.isNotBlank() && state.confirmPassword == state.password,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Continue")
    }
}

@Composable
fun ActivationContentStepFour(
    state: ActivationState,
    onFinish: () -> Unit
) {
    Text("Create 4-digit PIN", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(16.dp))
    OutlinedTextField(
        value = state.pin,
        onValueChange = { if (it.length <= 4) state.onPinChange },
        label = { Text("New PIN") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    OutlinedTextField(
        value = state.confirmPin,
        onValueChange = { if (it.length <= 4) state.onConfirmPinChange },
        label = { Text("Confirm PIN") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = onFinish,
        enabled = state.pin.length == 4 && state.confirmPin.length == 4,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Finish")
    }
}

@Preview(showBackground = true)
@Composable
fun ActivationStepOnePreview() {
    ArkheTheme {
        ActivationContentStepOne(
            state = rememberActivationState(),
            onNext = {}
        )
    }
}