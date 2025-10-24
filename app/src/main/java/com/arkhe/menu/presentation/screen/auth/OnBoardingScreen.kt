package com.arkhe.menu.presentation.screen.auth

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arkhe.menu.R
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.data.local.preferences.ProfilePicturePrefs
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.auth.activation.ActivationBottomSheet
import com.arkhe.menu.presentation.screen.auth.lockscreen.PinLockBottomSheet
import com.arkhe.menu.presentation.screen.auth.onboarding.OnBoardingUI
import com.arkhe.menu.presentation.screen.auth.signin.SignInBottomSheet
import com.arkhe.menu.presentation.ui.components.settings.PhotoProfile
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratAlternatesFontFamily
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Bulb
import compose.icons.evaicons.outline.LogIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    onNavigateToMain: () -> Unit,
    langViewModel: LanguageViewModel = koinViewModel(),
    themeViewModel: ThemeViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val currentThemeModel by themeViewModel.currentTheme.collectAsState(initial = ThemeModels.SYSTEM)

    /*---------- state of the datastore ----------*/
    val isActivated by authViewModel.isActivatedFlow.collectAsState(initial = false)
    val isSignedIn by authViewModel.isSignedInFlow.collectAsState(initial = false)

    /*---------- state for bottom sheet ----------*/
    var showActivationSheet by remember { mutableStateOf(false) }
    var showSignedInSheet by remember { mutableStateOf(false) }
    var showPinSheet by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    val images = listOf(
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3,
    )
    val texts = listOf(
        "Welcome to Our App!\nDiscover amazing features.",
        "Secure and Easy to Use\nSign in effortlessly.",
        "Get Started Today\nActivate your account."
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            coroutineScope.launch {
                currentIndex = (currentIndex + 1) % images.size
            }
        }
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    /*Layout 1*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.75f)
        ) {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    slideInHorizontally(animationSpec = tween(1000)) { width -> width } + fadeIn(
                        animationSpec = tween(1000)
                    ) togetherWith
                            slideOutHorizontally(animationSpec = tween(1000)) { width -> -width } + fadeOut(
                        animationSpec = tween(1000)
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { index ->
                Image(
                    painter = painterResource(id = images[index]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = texts[currentIndex],
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 24.sp,
                    fontFamily = montserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
            }
        }

        /*Layout 2*/
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TripkeunText(
                isActivation = isActivated,
                isSignedIn = isSignedIn,
                currentThemeModel = currentThemeModel,
                onActivationClick = {
                    showActivationSheet = true
                },
                onSignedInClick = {
                    showSignedInSheet = true
                },
                onPinUnlockClick = {
                    showPinSheet = true
                },
                onBoardingSettingsClick = {
                    showSettings = true
                }
            )
            OnboardingFooter(
                langViewModel = langViewModel,
                onAboutClick = {
                    navController.navigate(
                        NavigationRoute.aboutDetail(
                            source = NavigationRoute.ON_BOARDING
                        )
                    )
                },
                onPrivacyPolicyClick = {
                    navController.navigate(
                        NavigationRoute.privacyPolicyDetail(
                            source = NavigationRoute.ON_BOARDING
                        )
                    )
                },
                onTermsOfServiceClick = {
                    navController.navigate(
                        NavigationRoute.termOfServiceDetail(
                            source = NavigationRoute.ON_BOARDING
                        )
                    )
                }
            )
        }
    }

    OnBoardingUI(
        showSettings = showSettings,
        currentThemeModel = currentThemeModel,
        onDismissAll = {
            showActivationSheet = false
            showSignedInSheet = false
            showPinSheet = false
            showSettings = false
        }
    )

    if (showActivationSheet) {
        ActivationBottomSheet(
            onDismiss = { showActivationSheet = false },
            onActivated = {
                showActivationSheet = false
            },
            authViewModel = authViewModel
        )
    }

    if (showSignedInSheet) {
        SignInBottomSheet(
            onDismiss = { showSignedInSheet = false },
            onSignedIn = { sessionActivation, userId, password ->
                authViewModel.signIn(sessionActivation, userId, password)
            }
        )
    }

    if (showPinSheet) {
        PinLockBottomSheet(
            onDismiss = { showPinSheet = false },
            onPinEntered = {
                showPinSheet = false
                onNavigateToMain()
            }
        )
    }
}

@Composable
fun TripkeunText(
    isActivation: Boolean,
    isSignedIn: Boolean,
    currentThemeModel: ThemeModels,
    onActivationClick: () -> Unit,
    onSignedInClick: () -> Unit,
    onPinUnlockClick: () -> Unit,
    onBoardingSettingsClick: () -> Unit
) {
    val isDark = when (currentThemeModel) {
        ThemeModels.DARK -> true
        ThemeModels.LIGHT -> false
        ThemeModels.SYSTEM -> isSystemInDarkTheme()
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(110.dp)
                .padding(top = 4.dp),
            shape = MaterialTheme.shapes.large,
            color = Color.Transparent
        ) {
            if (isDark)
                DarkThemeBox(
                    onBoardingSettingsClick = onBoardingSettingsClick
                )
            else
                LightThemeBox(
                    onBoardingSettingsClick = onBoardingSettingsClick
                )
        }
        OnBoardingButton(
            isActivation = isActivation,
            isSignedIn = isSignedIn,
            onActivationClick = onActivationClick,
            onSignedInClick = onSignedInClick,
            onStartClick = onPinUnlockClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (38).dp)
        )
    }
}

@Composable
fun DarkThemeBox(
    onBoardingSettingsClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF170C10))
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        TextButton(
            onClick = onBoardingSettingsClick
        ) {
            Text(
                text = "tripkeun",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratAlternatesFontFamily,
                color = Color(0xFFEA508C)
            )
        }
    }
}

@Composable
fun LightThemeBox(
    onBoardingSettingsClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F5F2))
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        TextButton(
            onClick = onBoardingSettingsClick
        ) {
            Text(
                text = "tripkeun",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratAlternatesFontFamily,
                color = Color(0xFF183C2C)
            )
        }
    }
}

@Composable
private fun OnBoardingButton(
    isActivation: Boolean,
    isSignedIn: Boolean,
    onActivationClick: () -> Unit,
    onSignedInClick: () -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profilePicturePrefs = remember(context) { ProfilePicturePrefs(context) }
    val savedUri by profilePicturePrefs.getProfilePicture().collectAsState(initial = null)
    var profileImageUri by remember(savedUri) { mutableStateOf(savedUri) }

    when {
        !isActivation -> {
            Button(
                onClick = onActivationClick,
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp)
                    .width(230.dp),
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
                        imageVector = EvaIcons.Outline.Bulb,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                    )
                    Text(
                        text = "Activation",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }

        !isSignedIn -> {
            Button(
                onClick = onSignedInClick,
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp)
                    .width(230.dp),
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
                    Text(
                        text = "Sign-in",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Icon(
                        imageVector = EvaIcons.Outline.LogIn,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp)
                    )
                }
            }
        }

        else -> {
            Button(
                onClick = onStartClick,
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp)
                    .width(270.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    PhotoProfile(
                        imageUri = profileImageUri,
                        isDefault = false,
                        size = 24.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start Activity",
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingFooter(
    langViewModel: LanguageViewModel = koinViewModel(),
    onAboutClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsOfServiceClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, bottom = 24.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.35f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ae),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp)
                )
                Text(
                    text = stringResource(id = R.string.gaenta),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    modifier = Modifier.clickable { onAboutClick() }
                )
            }
        }

        Row(
            modifier = Modifier
                .weight(0.65f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = langViewModel.getLocalized(Lang.PRIVACY_POLICY),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 10.sp,
                modifier = Modifier.clickable { onPrivacyPolicyClick() }
            )
            Text(
                text = "•",
                fontSize = 24.sp,
                color = Color.Green.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp)
            )
            Text(
                text = langViewModel.getLocalized(Lang.TERMS_OF_SERVICE),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 10.sp,
                modifier = Modifier.clickable { onTermsOfServiceClick() }
            )
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
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
            OnboardingScreen(
                navController = NavHostController(previewContext),
                onNavigateToMain = {},
                previewThemeModel = ThemeModels.LIGHT
            )
        }
    }
}*/

//@Preview(showBackground = true)
//@Composable
//fun TripkeunTextPreview() {
//    ArkheTheme(
//        currentTheme = ThemeModels.DARK
//    ) {
//        TripkeunText(
//            isActivation = false,
//            isSignedIn = false,
//            currentThemeModel = ThemeModels.DARK,
//            onActivationClick = {},
//            onSignedInClick = {},
//            onStartClick = {}
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
fun OnBoardingButtonPreview() {
    ArkheTheme(
        currentTheme = ThemeModels.DARK
    ) {
        OnBoardingButton(
            isActivation = true,
            isSignedIn = true,
            onActivationClick = {},
            onSignedInClick = {},
            onStartClick = {}
        )
    }
}