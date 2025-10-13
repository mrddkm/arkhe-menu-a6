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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.edit.AnimatedNumericKeypad
import com.arkhe.menu.presentation.ui.components.edit.PasswordRequirementsChecklist
import com.arkhe.menu.presentation.ui.components.edit.validatePassword
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_MAIL
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_PHONE
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_USER_ID
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Archive
import compose.icons.evaicons.outline.ArrowIosBack
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.CheckmarkCircle
import compose.icons.evaicons.outline.CloseCircle
import kotlinx.coroutines.delay

@Composable
fun ActivationContentStepOne(
    state: ActivationState,
    onNext: () -> Unit
) {
    val focusUserId = remember { FocusRequester() }
    val focusPhone = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusUserId.requestFocus()
    }
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("1 / 4", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = state.userId,
            onValueChange = { if (it.length <= 6) state.onUserIdChange(it) },
            label = { Text("Tripkeun ID") },
            placeholder = {
                Text(
                    text = PLACE_HOLDER_USER_ID,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
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
            onValueChange = { if (it.length <= 15) state.onPhoneChange(it) },
            label = { Text("Phone Number") },
            placeholder = {
                Text(
                    text = PLACE_HOLDER_PHONE,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
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
            value = state.email,
            onValueChange = state.onEmailChange,
            label = { Text("Email") },
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
                if (state.email.isNotEmpty()) {
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
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onNext,
                enabled = state.userId.isNotBlank() && state.phone.isNotBlank() && state.email.isNotBlank(),
            ) {
                Row(
                    modifier = Modifier
                        .width(65.dp)
                        .height(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Next")
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        imageVector = EvaIcons.Outline.ArrowIosForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun ActivationContentStepTwo(
    state: ActivationState,
    onVerify: () -> Unit,
    onBack: () -> Unit
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
        Text("2 / 4", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text("Enter Activation Code", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

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
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus1),
                textStyle = MaterialTheme.typography.titleLarge,
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
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus2),
                textStyle = MaterialTheme.typography.titleLarge,
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
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus3),
                textStyle = MaterialTheme.typography.titleLarge,
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
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .size(56.dp)
                    .focusRequester(focus4),
                textStyle = MaterialTheme.typography.titleLarge,
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
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onBack) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosBack,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Back")
                }
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onVerify,
                enabled = code.length == 4
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Next")
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun ActivationContentStepThree(
    state: ActivationState,
    onContinue: () -> Unit,
    onBack: () -> Unit
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

    val keyboardController = LocalSoftwareKeyboardController.current

    val allChecklistPassed by remember(state.password) {
        derivedStateOf { newStrength.score == 5 }
    }

    val passwordsMatch by remember(state.password, state.confirmPassword) {
        derivedStateOf {
            state.confirmPassword.isNotEmpty() && state.password == state.confirmPassword
        }
    }

    LaunchedEffect(Unit) { focusNew.requestFocus() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("3 / 4", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text("Create Password", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // ---------------------------
            // 🟢 Step 1: NEW PASSWORD
            // ---------------------------
            OutlinedTextField(
                value = state.password,
                onValueChange = state.onPasswordChange,
                shape = MaterialTheme.shapes.medium,
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .focusRequester(focusNew),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
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

            // Strength Bar & Checklist (visible while checklist NOT yet all passed)
            AnimatedVisibility(visible = state.password.isNotEmpty() && !allChecklistPassed) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
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
                            .clip(RoundedCornerShape(3.dp)),
                        color = newStrength.color,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )

                    Text(
                        text = "Strength: ${newStrength.label}",
                        color = newStrength.color,
                        style = MaterialTheme.typography.labelMedium
                    )

                    PasswordRequirementsChecklist(password = state.password)
                }
            }

            // ---------------------------
            // 🟡 Step 2: CONFIRM PASSWORD (only appear when checklist OK)
            // ---------------------------
            AnimatedVisibility(visible = allChecklistPassed) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = state.onConfirmPasswordChange,
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Confirm Password") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
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
                            state.confirmPassword.isEmpty() -> "Re-enter your new password"
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
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onBack) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosBack,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Back")
                }
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onContinue,
                enabled = passwordsMatch
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Next")
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosForward,
                        contentDescription = null
                    )
                }
            }
        }
    }

    LaunchedEffect(allChecklistPassed) {
        if (allChecklistPassed) {
            delay(150)
            focusConfirm.requestFocus()
            keyboardController?.show()
        }
    }
}

/*@Composable
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
}*/

@Composable
fun ActivationContentStepFour(
    state: ActivationState,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val maxLength = 4

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("4 / 4", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text("4-digit PIN", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                repeat(maxLength) { index ->
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

            Spacer(Modifier.height(28.dp))
            AnimatedNumericKeypad(
                onDigit = { digit ->
                    if (state.pin.length < maxLength) state.onPinChange(state.pin + digit)
                },
                onDelete = {
                    if (state.pin.isNotEmpty()) state.onPinChange(state.pin.dropLast(1))
                }
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onBack) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosBack,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Back")
                }
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = onFinish,
                enabled = state.pin.length == 4 && state.confirmPin.length == 4,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.CheckmarkCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Finish")
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

/*
@Preview(showBackground = true)
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

@Preview(showBackground = true)
@Composable
fun ActivationStepFourPreview() {
    ArkheTheme {
        ActivationContentStepFour(
            state = rememberActivationState(),
            onFinish = {},
            onBack = {}
        )
    }
}