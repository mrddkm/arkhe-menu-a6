package com.arkhe.menu.presentation.screen.settings.account

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.domain.model.PasswordData
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.presentation.ui.components.edit.EditEmailField
import com.arkhe.menu.presentation.ui.components.edit.EditPhoneField
import com.arkhe.menu.presentation.ui.components.edit.EditableField
import com.arkhe.menu.presentation.ui.components.edit.EditableInfoScreenBase
import com.arkhe.menu.presentation.ui.components.edit.validatePassword
import com.arkhe.menu.presentation.ui.components.settings.SettingsItem
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.samplePasswordData
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Info
import compose.icons.evaicons.outline.LogOut
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun SignInSecurityScreen(
    onBackClick: () -> Unit = {},
    onSignedOutScreenClick: () -> Unit = {},
    user: User,
    passwordData: PasswordData,
    onUserUpdate: (User) -> Unit,
    onPasswordUpdate: (PasswordData) -> Unit,
) {
    Scaffold { paddingValues ->
        SignInSecurityContentExt(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = onBackClick,
            onSignedOutScreenClick = onSignedOutScreenClick,
            user = user,
            passwordData = passwordData,
            onUserUpdate = onUserUpdate,
            onPasswordUpdate = onPasswordUpdate,
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInSecurityContentExt(
    modifier: Modifier = Modifier,
    onHandleBackNavigation: () -> Unit = {},
    onSignedOutScreenClick: () -> Unit = {},
    user: User,
    passwordData: PasswordData,
    onUserUpdate: (User) -> Unit,
    onPasswordUpdate: (PasswordData) -> Unit,
    langViewModel: LanguageViewModel = koinViewModel()
) {
    var userState by remember { mutableStateOf(user) }

    LaunchedEffect(user) {
        userState = user
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHandleBackNavigation) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = langViewModel.getLocalized(Lang.SIGN_IN_AND_SECURITY),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Settings and recommendations to keep your account secure",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(Modifier.height(8.dp))
        }
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                SettingsItem(
                    label = "UserID",
                    labelInfo = "Your primary account",
                    value = userState.userId,
                    showIcon = false,
                    onClick = {},
                )
                SettingsItem(
                    label = "NickName",
                    labelInfo = "You can edit in Personal Info",
                    value = userState.nickName,
                    showIcon = false,
                    onClick = {},
                )
                EditableInfoScreenBase(
                    title = "Change Mail",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = "Email",
                            valueLabel = userState.mail,
                            getValue = { it.mail },
                            applyChange = { old, new -> old.copy(mail = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditEmailField(
                                    label = "New Mail",
                                    value = value,
                                    onValueChange = onValueChange
                                )
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Change Phone",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = "Phone",
                            valueLabel = userState.phone,
                            getValue = { it.phone },
                            applyChange = { old, new -> old.copy(phone = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditPhoneField(
                                    label = "New Phone Number",
                                    value = value,
                                    onValueChange = onValueChange
                                )
                            }
                        )
                    )
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 8.dp, bottom = 0.dp, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "This is your account to sign-in to the application.",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                EditableInfoScreenBase(
                    title = "Change Password",
                    userData = passwordData,
                    onUserUpdate = onPasswordUpdate,
                    fields = listOf(
                        EditableField(
                            valueLabel = "Password",
                            info = "Last changed Jan 10, 2025",
                            getValue = { it.newPassword },
                            applyChange = { old, new -> old.copy(newPassword = new) },
                            isValid = { validatePassword(it).isStrongEnough },
                            editor = { value, onValueChange ->
                                OutlinedTextField(
                                    value = value,
                                    onValueChange = onValueChange,
                                    label = { Text("") })
                            }
                        )
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, bottom = 0.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onSignedOutScreenClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.LogOut,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Sign-out",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInSecurityScreenPreview() {
    val previewContext = LocalContext.current
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
        ArkheTheme(
            currentTheme = ThemeModels.LIGHT
        ) {
            SignInSecurityScreen(
                onBackClick = {},
                onSignedOutScreenClick = {},
                user = sampleUser,
                passwordData = samplePasswordData,
                onUserUpdate = {},
                onPasswordUpdate = {}
            )
        }
    }
}