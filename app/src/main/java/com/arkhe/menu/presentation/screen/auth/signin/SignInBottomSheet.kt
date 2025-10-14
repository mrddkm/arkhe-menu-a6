package com.arkhe.menu.presentation.screen.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.utils.Constants.MaxMinLength.MIN_LENGTH_PASSWORD
import com.arkhe.menu.utils.Constants.MaxMinLength.MIN_LENGTH_USER_ID
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_USER_ID
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.CloseCircle
import compose.icons.evaicons.outline.LogIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBottomSheet(
    onDismiss: () -> Unit,
    onSignedIn: (String, String) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        SignInContent(
            onDismiss = onDismiss, onSignedIn = onSignedIn
        )
    }
}

@Composable
fun SignInContent(
    state: SignInState = rememberSignInState(),
    onDismiss: () -> Unit,
    onSignedIn: (String, String) -> Unit
) {
    val focusUserId = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }
    var passwordVisible by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        focusUserId.requestFocus()
    }

    val isValid by remember(state.userId, state.password) {
        derivedStateOf {
            state.userId.length >= MIN_LENGTH_USER_ID && state.password.length >= MIN_LENGTH_PASSWORD
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sign In", style = MaterialTheme.typography.titleMedium)
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
                onNext = { focusPassword.requestFocus() }
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
        if (state.userId.length >= MIN_LENGTH_USER_ID){
            OutlinedTextField(
                value = state.password,
                onValueChange = state.onPasswordChange,
                shape = MaterialTheme.shapes.medium,
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .focusRequester(focusPassword),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Outlined.Abc else Icons.Outlined.Password
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onDismiss) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Forgot Password ?..",
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    )
                }
            }
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = { onSignedIn(state.userId, state.password) },
                enabled = isValid,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Sign-in")
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = EvaIcons.Outline.LogIn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun rememberSignInState(): SignInState {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    return remember {
        SignInState(
            userId = userId,
            onUserIdChange = { userId = it },
            password = password,
            onPasswordChange = { password = it }
        )
    }
}

data class SignInState(
    val userId: String,
    val onUserIdChange: (String) -> Unit,
    val password: String,
    val onPasswordChange: (String) -> Unit
)


@Preview(showBackground = true)
@Composable
fun SignInBottomSheetPreview() {
    ArkheTheme {
        SignInContent(
            onDismiss = {},
            onSignedIn = { _, _ -> }
        )
    }
}