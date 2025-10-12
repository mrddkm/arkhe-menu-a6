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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.R
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.montserratAlternatesFontFamily
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.ThemeViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Activity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingScreen(
    themeViewModel: ThemeViewModel = koinViewModel(),
    previewThemeModel: ThemeModels? = null
) {
    val themeFromVm by themeViewModel.currentTheme.collectAsState()
    val currentThemeModel = previewThemeModel ?: themeFromVm

    /*temporary state*/
    val isActivation = true
    val isLogin = true

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
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TripkeunText(
                isActivation = isActivation,
                isLogin = isLogin,
                currentThemeModel = currentThemeModel
            )
            OnboardingFooter()
        }
    }
}

@Composable
fun TripkeunText(
    isActivation: Boolean,
    isLogin: Boolean,
    currentThemeModel: ThemeModels
) {
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
            when (currentThemeModel) {
                ThemeModels.DARK -> {
                    DarkThemeBox()
                }

                ThemeModels.LIGHT -> {
                    LightThemeBox()
                }

                ThemeModels.SYSTEM -> {
                    if (isSystemInDarkTheme()) {
                        DarkThemeBox()
                    } else {
                        LightThemeBox()
                    }
                }
            }
        }
        OnBoardingButton(
            isActivation = isActivation,
            isLogin = isLogin,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (38).dp)
        )
    }
}

@Composable
private fun DarkThemeBox() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF170C10))
            .padding(top = 16.dp, bottom = 8.dp)
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

@Composable
private fun LightThemeBox() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F5F2))
            .padding(top = 16.dp, bottom = 8.dp)
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

@Composable
private fun OnBoardingButton(
    isActivation: Boolean,
    isLogin: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isActivation) {
        Button(
            onClick = { /* TODO: Handle Activation */ },
            modifier = modifier
                .padding(vertical = 16.dp)
                .height(48.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text("Activation")
        }
    } else {
        if (!isLogin) {
            Button(
                onClick = { /* TODO: Handle Login */ },
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text("Login")
            }
        } else {
            Button(
                onClick = { /* TODO: Handle Start Activity */ },
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.Activity,
                        contentDescription = "Start Activity",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Start Activity")
                }
            }
        }
    }
}

@Composable
private fun OnboardingFooter() {
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
                    modifier = Modifier.clickable { /* TODO: Handle About click */ }
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
                text = "Privacy policy",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 10.sp,
                modifier = Modifier.clickable { /* TODO: Handle Privacy Policy click */ }
            )
            Text(
                text = "•",
                fontSize = 24.sp,
                color = Color.Green.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp)
            )
            Text(
                text = "Terms of service",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 10.sp,
                modifier = Modifier.clickable { /* TODO: Handle Terms of Service click */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
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
        ArkheTheme(
            currentTheme = ThemeModels.LIGHT
        ) {
            OnboardingScreen(
                previewThemeModel = ThemeModels.LIGHT
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun TripkeunTextPreview() {
    ArkheTheme(
        currentTheme = ThemeModels.DARK
    ) {
        TripkeunText()
    }
}*/
