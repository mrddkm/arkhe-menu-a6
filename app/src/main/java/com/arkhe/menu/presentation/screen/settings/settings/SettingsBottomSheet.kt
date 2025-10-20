package com.arkhe.menu.presentation.screen.settings.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.data.local.preferences.ProfilePicturePrefs
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.components.settings.PhotoProfile
import com.arkhe.menu.presentation.ui.components.settings.SettingsBottomSheetItem
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Shield
import compose.icons.evaicons.outline.Smartphone
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun SettingsBottomSheet(
    themeViewModel: ThemeViewModel = koinViewModel(),
    langViewModel: LanguageViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
    onClose: () -> Unit = {},
    onLockScreenClick: () -> Unit = {},
    onPersonalInfoClick: () -> Unit = {},
    onSignInSecurityClick: () -> Unit = {},
    onDevicesClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsOfServiceClick: () -> Unit = {}
) {
    DisposableEffect(Unit) {
        langViewModel.setLanguageChangeCallbacks(
            onStarted = { mainViewModel.onLanguageChangeStarted() },
            onCompleted = { mainViewModel.onLanguageChangeCompleted() }
        )
        onDispose { }
    }

    val context = LocalContext.current
    val profilePicturePrefs = remember(context) { ProfilePicturePrefs(context) }
    val savedUri by profilePicturePrefs.getProfilePicture().collectAsState(initial = null)
    var profileImageUri by remember(savedUri) { mutableStateOf(savedUri) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onClose() }) {
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
            HeaderTitleSecondary(
                title = langViewModel.getLocalized(Lang.PROFILE_SETTINGS),
            )
            IconButton(onClick = { onLockScreenClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PhotoProfile(
                imageUri = profileImageUri
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Didik Muttaqien",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "didik.muttaqien@gmail.com",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "230504",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = sourceCodeProFontFamily
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                SettingsBottomSheetItem(
                    label = langViewModel.getLocalized(Lang.PERSONAL_INFO),
                    labelInfo = langViewModel.getLocalized(Lang.PERSONAL_INFO_DESC),
                    icon = EvaIcons.Outline.Person,
                    onClick = { onPersonalInfoClick() }
                )
                SettingsBottomSheetItem(
                    label = langViewModel.getLocalized(Lang.SIGN_IN_AND_SECURITY),
                    labelInfo = langViewModel.getLocalized(Lang.SIGN_IN_AND_SECURITY_DESC),
                    icon = EvaIcons.Outline.Shield,
                    onClick = { onSignInSecurityClick() }
                )
                SettingsBottomSheetItem(
                    label = langViewModel.getLocalized(Lang.DEVICES),
                    labelInfo = langViewModel.getLocalized(Lang.DEVICES_DESC),
                    icon = EvaIcons.Outline.Smartphone,
                    onClick = { onDevicesClick() },
                    showDivider = false
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            SettingsTabs(
                themeViewModel = themeViewModel,
                langViewModel = langViewModel
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                SettingsBottomSheetItem(
                    label = langViewModel.getLocalized(Lang.ABOUT),
                    painter = painterResource(R.drawable.ic_ae),
                    onClick = { onAboutClick() },
                    showDivider = false
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = langViewModel.getLocalized(Lang.PRIVACY_POLICY),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.clickable { onPrivacyPolicyClick() }
            )
            Text(
                text = langViewModel.getLocalized(Lang.TERMS_OF_SERVICE),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.clickable { onTermsOfServiceClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserBottomSheetPreview() {
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
            SettingsBottomSheet()
        }
    }
}