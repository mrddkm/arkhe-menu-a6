@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.presentation.screen.settings.account.AccountItem
import com.arkhe.menu.presentation.ui.components.ArkheLanguageButton
import com.arkhe.menu.presentation.ui.components.ArkheThemeButtons
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ColorPalette
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Settings
import compose.icons.evaicons.outline.Shield
import compose.icons.evaicons.outline.Smartphone
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun SettingsBottomSheet(
    themeViewModel: ThemeViewModel = koinViewModel(),
    languageViewModel: LanguageViewModel = koinViewModel()
) {
    val currentTheme by themeViewModel.currentTheme.collectAsState()
    val languageState by languageViewModel.languageState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(48.dp))
            HeaderTitleSecondary(
                title = languageViewModel.getLocalizedString("profile_settings"),
            )
            IconButton(onClick = {}) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
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
                AccountItem(
                    label = "Personal Info",
                    labelInfo = "Name, Phone & Profile",
                    icon = EvaIcons.Outline.Person,
                    onClick = {}
                )
                AccountItem(
                    label = "Sign-in & Security",
                    labelInfo = "Email, Password & PIN",
                    icon = EvaIcons.Outline.Shield,
                    onClick = {}
                )
                AccountItem(
                    label = "Devices",
                    labelInfo = "Keep track of your devices",
                    icon = EvaIcons.Outline.Smartphone,
                    onClick = {},
                    showDivider = false
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 10.dp, end = 0.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.Gray
                        )
                    }
                    Text(
                        text = "Language",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            ArkheLanguageButton(
                selectedLanguage = languageState.currentLanguage,
                onLanguageSelected = { pickLanguage ->
                    languageViewModel.selectLanguage(pickLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 10.dp, end = 0.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.ColorPalette,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.Gray
                        )
                    }
                    Text(
                        text = "Theme",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                ArkheThemeButtons(
                    currentTheme = currentTheme,
                    onThemeSelected = { pickTheme ->
                        themeViewModel.setTheme(pickTheme)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                AccountItem(
                    label = "About",
                    labelInfo = "Gaenta, Arkhe & Support",
                    painter = painterResource(R.drawable.ic_ae),
                    onClick = {},
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
                text = "Privacy Policy",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.clickable {}
            )
            Text(
                text = "Terms of Service",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.clickable {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserBottomSheetPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule
            )
        }
    ) {
        ArkheTheme {
            SettingsBottomSheet()
        }
    }
}