package com.arkhe.menu.presentation.screen.auth.signin

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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.Constants.MaxMinLength.MIN_LENGTH_PASSWORD
import com.arkhe.menu.utils.Constants.MaxMinLength.MIN_LENGTH_USER_ID
import com.arkhe.menu.utils.Constants.TextPlaceHolder.PLACE_HOLDER_OTHER_USER_ID
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.CloseCircle
import compose.icons.evaicons.outline.LogIn
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInBottomSheet(
    onDismiss: () -> Unit,
    onSignedIn: (String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.Lock,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        SignInContent(
            onSignedIn = onSignedIn
        )
    }
}

@Composable
fun SignInContent(
    state: SignInState = rememberSignInState(),
    langViewModel: LanguageViewModel = koinViewModel(),
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
        Text(
            text = langViewModel.getLocalized(Lang.SIGN_IN),
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = state.userId,
            onValueChange = { state.onUserIdChange(it) },
            label = { Text("Account") },
            placeholder = {
                Text(
                    text = PLACE_HOLDER_OTHER_USER_ID,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
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
        if (state.userId.length >= MIN_LENGTH_USER_ID) {
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
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = {},
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Forgot Password ?..",
                        fontSize = 10.sp,
                    )
                }
            }
            Button(
                onClick = { onSignedIn(state.userId.trim(), state.password) },
                enabled = isValid,
                modifier = Modifier
                    .width(130.dp)
                    .height(40.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun rememberSignInState(): SignInState {
    var userId by remember { mutableStateOf("230504") }
    var password by remember { mutableStateOf("Qwer@123") }

    return SignInState(
        userId = userId,
        onUserIdChange = { newValue ->
            userId = newValue.trimStart()
        },
        password = password,
        onPasswordChange = { password = it }
    )

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
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule,
                previewModule
            )
        }
    ) {
        ArkheTheme {
            SignInContent(
                onSignedIn = { _, _ -> }
            )
        }
    }
}