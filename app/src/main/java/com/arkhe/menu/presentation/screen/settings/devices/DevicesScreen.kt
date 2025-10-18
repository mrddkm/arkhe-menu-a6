package com.arkhe.menu.presentation.screen.settings.devices

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.arkhe.menu.domain.model.PinData
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.presentation.ui.components.edit.AnimatedPinField
import com.arkhe.menu.presentation.ui.components.edit.EditableField
import com.arkhe.menu.presentation.ui.components.edit.EditableInfoScreenBase
import com.arkhe.menu.presentation.ui.components.settings.SettingsToggleItem
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.samplePinData
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun DevicesScreen(
    onBackClick: () -> Unit,
    onDeactivationClick: () -> Unit = {},
    user: User,
    onUserUpdate: (User) -> Unit,
    pinData: PinData,
    onPinUpdate: (PinData) -> Unit
) {
    Scaffold { paddingValues ->
        DevicesContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = onBackClick,
            onDeactivationClick = onDeactivationClick,
            user = user,
            onUserUpdate = onUserUpdate,
            pinData = pinData,
            onPinUpdate = onPinUpdate
        )
    }
}

@Composable
fun DevicesContent(
    modifier: Modifier = Modifier,
    onHandleBackNavigation: () -> Unit = { },
    onDeactivationClick: () -> Unit = {},
    user: User,
    onUserUpdate: (User) -> Unit,
    pinData: PinData,
    onPinUpdate: (PinData) -> Unit,
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
                text = langViewModel.getLocalized(Lang.DEVICES),
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
                EditableInfoScreenBase(
                    title = "Change PIN",
                    userData = pinData,
                    onUserUpdate = onPinUpdate,
                    fields = listOf(
                        EditableField(
                            valueLabel = "PIN Lock",
                            info = "Last changed Jan 10, 2025",
                            showDivider = false,
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
            }
        }
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            SettingsToggleItem(
                value = "Biometric",
                showDivider = false,
                isActive = userState.isBiometricActive,
                onToggle = { newValue ->
                    val updatedUser = userState.copy(isBiometricActive = newValue)
                    userState = updatedUser
                    onUserUpdate(updatedUser)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp, bottom = 0.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onDeactivationClick,
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
                        imageVector = Icons.Outlined.LockReset,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Deactivation",
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
fun DevicesScreenPreview() {
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
            DevicesScreen(
                onBackClick = {},
                user = sampleUser,
                onUserUpdate = {},
                pinData = samplePinData,
                onPinUpdate = {}
            )
        }
    }
}