package com.arkhe.menu.presentation.screen.auth.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.presentation.screen.auth.DarkThemeBox
import com.arkhe.menu.presentation.screen.auth.LightThemeBox
import com.arkhe.menu.presentation.screen.settings.settings.SettingsTabs
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.Close
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingSettingsBottomSheet(
    onDismiss: () -> Unit,
    currentThemeModel: ThemeModels,
    langViewModel: LanguageViewModel = koinViewModel(),
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
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                HeaderTitleSecondary(
                    title = langViewModel.getLocalized(Lang.PROFILE_SETTINGS),
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
        OnBoardingSettingsContent(
            currentThemeModel = currentThemeModel,
        )
    }
}

@Composable
fun OnBoardingSettingsContent(
    currentThemeModel: ThemeModels,
    themeViewModel: ThemeViewModel = koinViewModel(),
    langViewModel: LanguageViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
) {
    DisposableEffect(Unit) {
        langViewModel.setLanguageChangeCallbacks(
            onStarted = { mainViewModel.onLanguageChangeStarted() },
            onCompleted = { mainViewModel.onLanguageChangeCompleted() }
        )
        onDispose { }
    }

    val isDark = when (currentThemeModel) {
        ThemeModels.DARK -> true
        ThemeModels.LIGHT -> false
        ThemeModels.SYSTEM -> isSystemInDarkTheme()
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
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(110.dp)
                        .padding(top = 4.dp),
                    shape = MaterialTheme.shapes.large,
                    color = Color.Transparent
                ) {
                    if (isDark)
                        DarkThemeBox()
                    else
                        LightThemeBox()
                }
                Text(
                    text = "“ \uD83D\uDCAD Kamanakeun, tripkeun! • tripkeun, gaassskeun! ”",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = montserratFontFamily,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .offset(y = (25).dp),
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
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingBottomSheetPreview() {
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
            OnBoardingSettingsContent(
                currentThemeModel = ThemeModels.SYSTEM,
            )
        }
    }
}