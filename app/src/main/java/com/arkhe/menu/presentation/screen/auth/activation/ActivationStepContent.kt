package com.arkhe.menu.presentation.screen.auth.activation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.LoadingGraySpinner
import com.arkhe.menu.presentation.ui.components.edit.AnimatedNumericKeypad
import com.arkhe.menu.presentation.ui.components.edit.PasswordRequirementsChecklist
import com.arkhe.menu.presentation.ui.components.edit.validatePassword
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.presentation.ui.theme.trafficLights
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.utils.Constants.MaxMinLength.MAX_LENGTH_PIN
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_MAIL
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_PHONE
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_PRIMARY_USER_ID
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.CheckmarkCircle
import compose.icons.evaicons.outline.CloseCircle

@Composable
fun ActivationContentStepOne(
    state: ActivationState,
    uiState: AuthUiState,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onNext: () -> Unit
) {
    val focusUserId = remember { FocusRequester() }
    val focusPhone = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusUserId.requestFocus()
    }
    val isLoading = uiState is AuthUiState.Loading
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Please fill your information",
                style = MaterialTheme.typography.titleSmall
            )
            if (uiState is AuthUiState.Failed) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.trafficLights.stop,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        OutlinedTextField(
            value = state.userId,
            onValueChange = {
                if (it.length <= 6) state.onUserIdChange(it)
                if (uiState is AuthUiState.Failed) {
                    authViewModel.resetUiState()
                }
            },
            label = { Text("Tripkeun ID") },
            enabled = !isLoading,
            placeholder = {
                Text(
                    text = PLACE_HOLDER_PRIMARY_USER_ID,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusPhone.requestFocus() }
            ),
            trailingIcon = {
                if (state.userId.isNotEmpty()) {
                    IconButton(onClick = { state.onUserIdChange("") }) {
                        Icon(
                            imageVector = EvaIcons.Outline.CloseCircle,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .focusRequester(focusUserId)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) keyboardController?.show()
                },
        )
        OutlinedTextField(
            value = state.phone,
            onValueChange = {
                if (it.length <= 15) state.onPhoneChange(it)
                if (uiState is AuthUiState.Failed) {
                    authViewModel.resetUiState()
                }
            },
            label = { Text("Phone Number") },
            enabled = !isLoading,
            placeholder = {
                Text(
                    text = PLACE_HOLDER_PHONE,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusEmail.requestFocus() }
            ),
            trailingIcon = {
                if (state.phone.isNotEmpty()) {
                    IconButton(onClick = { state.onPhoneChange("") }) {
                        Icon(
                            imageVector = EvaIcons.Outline.CloseCircle,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .focusRequester(focusPhone)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) keyboardController?.show()
                },
        )
        OutlinedTextField(
            value = state.mail,
            onValueChange = { newValue ->
                state.onEmailChange(newValue)
                if (uiState is AuthUiState.Failed) {
                    authViewModel.resetUiState()
                }
            },
            label = { Text("Email") },
            enabled = !isLoading,
            placeholder = {
                Text(
                    text = PLACE_HOLDER_MAIL,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                if (state.mail.isNotEmpty()) {
                    IconButton(onClick = { state.onEmailChange("") }) {
                        Icon(
                            imageVector = EvaIcons.Outline.CloseCircle,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .focusRequester(focusEmail)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) keyboardController?.show()
                },
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = onNext,
                enabled = state.userId.isNotBlank() &&
                        state.phone.isNotBlank() &&
                        state.mail.isNotBlank() &&
                        uiState !is AuthUiState.Loading,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState is AuthUiState.Loading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingGraySpinner(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Next")
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = EvaIcons.Outline.ArrowIosForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivationContentStepTwo(
    state: ActivationState,
    uiState: AuthUiState,
    authViewModel: AuthViewModel,
    onVerify: () -> Unit
) {
    val focus1 = remember { FocusRequester() }
    val focus2 = remember { FocusRequester() }
    val focus3 = remember { FocusRequester() }
    val focus4 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var code1 by remember { mutableStateOf("") }
    var code2 by remember { mutableStateOf("") }
    var code3 by remember { mutableStateOf("") }
    var code4 by remember { mutableStateOf("") }
    val isLoading = uiState is AuthUiState.Loading
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Failed) {
            code1 = ""
            code2 = ""
            code3 = ""
            code4 = ""
            focus1.requestFocus()
        }
    }
    LaunchedEffect(Unit) {
        focus1.requestFocus()
    }
    val code = code1 + code2 + code3 + code4
    LaunchedEffect(code) {
        state.onCodeChange(code)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (state.userName.isNotBlank()) {
                Text(
                    text = "Hello, ${state.userName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
            if (state.successMessage.isNotBlank()) {
                Text(
                    text = state.successMessage,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            if (uiState is AuthUiState.Failed) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.trafficLights.stop,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedTextField(
                value = code1,
                onValueChange = {
                    if (it.length <= 1 && it.all(Char::isDigit)) {
                        code1 = it
                        if (it.isNotEmpty()) focus2.requestFocus()
                    }
                    if (uiState is AuthUiState.Failed) {
                        authViewModel.resetUiState()
                    }
                },
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus1),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontFamily = sourceCodeProFontFamily
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = code2,
                onValueChange = {
                    if (it.length <= 1 && it.all(Char::isDigit)) {
                        code2 = it
                        if (it.isNotEmpty()) focus3.requestFocus()
                        else focus1.requestFocus()
                    } else if (it.isEmpty()) {
                        focus1.requestFocus()
                    }
                },
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus2),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontFamily = sourceCodeProFontFamily
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = code3,
                onValueChange = {
                    if (it.length <= 1 && it.all(Char::isDigit)) {
                        code3 = it
                        if (it.isNotEmpty()) focus4.requestFocus()
                        else focus2.requestFocus()
                    } else if (it.isEmpty()) {
                        focus2.requestFocus()
                    }
                },
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus3),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontFamily = sourceCodeProFontFamily
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = code4,
                onValueChange = {
                    if (it.length <= 1 && it.all(Char::isDigit)) {
                        code4 = it
                        if (it.isEmpty()) focus3.requestFocus()
                        else keyboardController?.hide()
                    } else if (it.isEmpty()) {
                        focus3.requestFocus()
                    }
                },
                enabled = !isLoading,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus4),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                    fontFamily = sourceCodeProFontFamily
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onVerify,
                enabled = code.length == 4,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingGraySpinner(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Verify")
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = EvaIcons.Outline.ArrowIosForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivationContentStepThree(
    state: ActivationState,
    uiState: AuthUiState,
    authViewModel: AuthViewModel,
    onContinue: () -> Unit
) {
    val newStrength by remember(state.password) {
        derivedStateOf {
            validatePassword(
                state.password
            )
        }
    }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    val focusNew = remember { FocusRequester() }
    val focusConfirm = remember { FocusRequester() }

    val allChecklistPassed by remember(state.password) {
        derivedStateOf { newStrength.score == 5 }
    }

    val passwordsMatch by remember(state.password, state.confirmPassword) {
        derivedStateOf {
            state.confirmPassword.isNotEmpty() && state.password == state.confirmPassword
        }
    }
    val isLoading = uiState is AuthUiState.Loading

    LaunchedEffect(Unit) { focusNew.requestFocus() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Create Password",
                style = MaterialTheme.typography.titleSmall
            )
            if (uiState is AuthUiState.Failed) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.trafficLights.stop,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // ---------------------------
            // 🟢 Step 1: NEW PASSWORD
            // ---------------------------
            OutlinedTextField(
                value = state.password,
                onValueChange = { newValue ->
                    state.onPasswordChange(newValue)
                    if (uiState is AuthUiState.Failed) {
                        authViewModel.resetUiState()
                    }
                },
                shape = MaterialTheme.shapes.medium,
                label = { Text("Password") },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .focusRequester(focusNew),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusConfirm.requestFocus() }
                ),
                visualTransformation = if (newVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (newVisible) Icons.Outlined.Abc else Icons.Outlined.Password
                    IconButton(onClick = { newVisible = !newVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )

            AnimatedVisibility(visible = state.password.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val animatedProgress by animateFloatAsState(
                        targetValue = newStrength.score / 5f,
                        animationSpec = tween(400),
                        label = "strengthAnim"
                    )

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .padding(start = 16.dp, end = 16.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = newStrength.color,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = newStrength.label,
                            color = newStrength.color,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    AnimatedVisibility(visible = !allChecklistPassed) {
                        PasswordRequirementsChecklist(password = state.password)
                    }
                }
            }

            // ---------------------------
            // 🟡 Step 2: CONFIRM PASSWORD (only appear when checklist OK)
            // ---------------------------
            AnimatedVisibility(visible = allChecklistPassed) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    /*Spacer(Modifier.height(6.dp))*/
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { newValue ->
                            state.onConfirmPasswordChange(newValue)
                            if (uiState is AuthUiState.Failed) {
                                authViewModel.resetUiState()
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Confirm Password") },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusConfirm),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon =
                                if (confirmVisible) Icons.Outlined.Abc else Icons.Outlined.Password
                            IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    val matchColor by animateColorAsState(
                        if (passwordsMatch) MaterialTheme.colorScheme.onSurface else Color.Gray,
                        label = "matchColor"
                    )

                    Text(
                        text = when {
                            state.confirmPassword.isEmpty() -> "Re-enter your password"
                            passwordsMatch -> "Passwords match"
                            else -> "Passwords do not match"
                        },
                        color = matchColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onContinue,
                enabled = passwordsMatch,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingGraySpinner(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Submit")
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            imageVector = EvaIcons.Outline.ArrowIosForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivationContentStepFour(
    state: ActivationState,
    uiState: AuthUiState,
    onFinish: () -> Unit
) {
    val labelCreatePIN = "Create PIN"
    val labelConfirmPIN = "Confirm PIN"

    val creatingPin = state.pin.length < MAX_LENGTH_PIN
    val confirmingPin = state.pin.length == MAX_LENGTH_PIN

    val pinMatch = state.pin == state.confirmPin && state.confirmPin.length == MAX_LENGTH_PIN
    val pinMismatch = state.confirmPin.length == MAX_LENGTH_PIN && state.pin != state.confirmPin

    val isLoading = uiState is AuthUiState.Loading

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "4-digit PIN",
                style = MaterialTheme.typography.titleSmall
            )
            if (uiState is AuthUiState.Failed) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.trafficLights.stop,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        /*CREATE PIN SECTION*/
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (creatingPin) {
                Text(
                    labelCreatePIN,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    repeat(MAX_LENGTH_PIN) { index ->
                        val filled = index < state.pin.length
                        val scale by animateFloatAsState(
                            targetValue = if (filled) 1.2f else 1f,
                            animationSpec = spring(
                                dampingRatio = 0.4f,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "pinDotScale"
                        )
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .background(
                                    if (filled) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    else Color.LightGray,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.CheckmarkCircle,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Green.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        labelCreatePIN,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.DarkGray
                    )
                }
            }
        }

        /*CONFIRM PIN SECTION*/
        if (confirmingPin) {
            Spacer(Modifier.height(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!pinMatch) {
                    Text(
                        labelConfirmPIN,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.DarkGray
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        repeat(MAX_LENGTH_PIN) { index ->
                            val filled = index < state.confirmPin.length
                            val scale by animateFloatAsState(
                                targetValue = if (filled) 1.2f else 1f,
                                animationSpec = spring(
                                    dampingRatio = 0.4f,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "confirmPinDotScale"
                            )
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .background(
                                        if (filled) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        else Color.LightGray,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.CheckmarkCircle,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Green.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            labelConfirmPIN,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.DarkGray
                        )
                    }
                }

                if (pinMismatch) {
                    Text(
                        text = "PIN not match",
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall.copy()
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        AnimatedNumericKeypad(
            onDigit = { digit ->
                if (state.pin.length < MAX_LENGTH_PIN) {
                    state.onPinChange(state.pin + digit)
                } else if (state.confirmPin.length < MAX_LENGTH_PIN) {
                    state.onConfirmPinChange(state.confirmPin + digit)
                }
            },
            onDelete = {
                if (state.confirmPin.isNotEmpty()) {
                    state.onConfirmPinChange(state.confirmPin.dropLast(1))
                } else if (state.pin.isNotEmpty()) {
                    state.onPinChange(state.pin.dropLast(1))
                }
            }
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onFinish,
                enabled = pinMatch,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LoadingGraySpinner(modifier = Modifier.fillMaxSize())
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.CheckmarkCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun ActivationStepOnePreview() {
    ArkheTheme {
        ActivationContentStepOne(
            state = rememberActivationState(),
            onDismiss = {},
            onNext = {}
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun ActivationStepTwoPreview() {
    ArkheTheme {
        ActivationContentStepTwo(
            state = rememberActivationState(),
            onVerify = {},
            onBack = {}
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun ActivationStepThreePreview() {
    ArkheTheme {
        ActivationContentStepThree(
            state = rememberActivationState(),
            onContinue = {},
            onBack = {}
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun ActivationStepFourPreview() {
    ArkheTheme {
        ActivationContentStepFour(
            state = rememberActivationState(),
            onFinish = {},
            onBack = {}
        )
    }
}*/
