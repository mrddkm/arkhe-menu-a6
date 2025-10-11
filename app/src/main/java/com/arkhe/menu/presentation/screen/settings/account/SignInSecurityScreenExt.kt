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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.domain.model.PasswordData
import com.arkhe.menu.domain.model.PinData
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.settings.account.components.AccountItem
import com.arkhe.menu.presentation.screen.settings.account.components.AccountToggleItem
import com.arkhe.menu.presentation.ui.components.edit.AnimatedPinField
import com.arkhe.menu.presentation.ui.components.edit.EditEmailField
import com.arkhe.menu.presentation.ui.components.edit.EditPhoneField
import com.arkhe.menu.presentation.ui.components.edit.EditableField
import com.arkhe.menu.presentation.ui.components.edit.EditableInfoScreenBase
import com.arkhe.menu.presentation.ui.components.edit.validatePassword
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.samplePasswordData
import com.arkhe.menu.utils.samplePinData
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Info
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun SignInSecurityScreenExt(
    onBackClick: () -> Unit,
    navController: NavController? = null,
    user: User,
    passwordData: PasswordData,
    pinData: PinData,
    onUserUpdate: (User) -> Unit,
    onPasswordUpdate: (PasswordData) -> Unit,
    onPinUpdate: (PinData) -> Unit
) {
    val handleBackNavigation: () -> Unit = {
        navController?.let { nav ->
            val popSuccess = nav.popBackStack()
            if (!popSuccess) {
                nav.navigate(NavigationRoute.MAIN) {
                    popUpTo(NavigationRoute.MAIN) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        } ?: run {
            onBackClick()
        }
    }

    Scaffold { paddingValues ->
        SignInSecurityContentExt(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = handleBackNavigation,
            user = user,
            passwordData = passwordData,
            pinData = pinData,
            onUserUpdate = onUserUpdate,
            onPasswordUpdate = onPasswordUpdate,
            onPinUpdate = onPinUpdate
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignInSecurityContentExt(
    modifier: Modifier = Modifier,
    user: User,
    passwordData: PasswordData,
    pinData: PinData,
    onUserUpdate: (User) -> Unit,
    onPasswordUpdate: (PasswordData) -> Unit,
    onPinUpdate: (PinData) -> Unit,
    onHandleBackNavigation: () -> Unit = { },
    langViewModel: LanguageViewModel = koinViewModel()
) {
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
                AccountItem(
                    label = "UserID",
                    value = user.userId,
                    info = "Your primary account",
                    onClick = {},
                    showIcon = false
                )
                AccountItem(
                    label = "NickName",
                    value = user.nickName,
                    info = "You can edit in Personal Info",
                    onClick = {},
                    showIcon = false
                )
                EditableInfoScreenBase(
                    title = "Change Mail",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = "Email",
                            valueLabel = user.mail,
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
                            valueLabel = user.phone,
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
                            label = "Password",
                            valueLabel = "Password",
                            info = "Last changed Jan 10, 2025",
                            getValue = { it.newPassword },
                            applyChange = { old, new -> old.copy(newPassword = new) },
                            isValid = { validatePassword(it).isStrongEnough },
                            editor = { value, onValueChange ->
//                                EditPasswordFieldWithStrength(
//                                    labelNewPassword = "New Password",
//                                    valueNewPassword = value,
//                                    labelConfirmPassword = "Confirm Password",
//                                    valueConfirmPassword = passwordData.confirmPassword,
//                                    onNewPasswordChange = onValueChange,
//                                    onConfirmPasswordChange = {}
//                                )
                                OutlinedTextField(
                                    value = value,
                                    onValueChange = onValueChange,
                                    label = { Text("Password") })
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Change PIN",
                    userData = pinData,
                    onUserUpdate = onPinUpdate,
                    fields = listOf(
                        EditableField(
                            valueLabel = "PIN Lock",
                            info = "Last changed Jan 10, 2025",
                            getValue = { it.newPin },
                            applyChange = { old, new -> old.copy(newPin = new) },
                            isValid = { it.length == 4 },
                            editor = { value, onValueChange ->
                                AnimatedPinField(
                                    label = "Enter New PIN",
                                    value = value,
                                    onValueChange = onValueChange
                                )
                            }
                        )
                    )
                )
                AccountToggleItem(
                    value = "Biometric",
                    showDivider = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInSecurityScreenExtPreview() {
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
        ArkheTheme {
            SignInSecurityScreenExt(
                onBackClick = {},
                navController = null,
                user = sampleUser,
                passwordData = samplePasswordData,
                pinData = samplePinData,
                onUserUpdate = {},
                onPasswordUpdate = {},
                onPinUpdate = {}
            )
        }
    }
}