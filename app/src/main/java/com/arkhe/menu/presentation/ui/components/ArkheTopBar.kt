package com.arkhe.menu.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.R
import com.arkhe.menu.data.local.preferences.ProfilePicturePrefs
import com.arkhe.menu.presentation.ui.components.settings.PhotoProfile
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArkheTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isInMainContent: Boolean,
    currentContentType: String,
    onBackClick: () -> Unit,
    onProfileSettingsClick: () -> Unit
) {
    val offset = scrollBehavior.state.contentOffset
    val isScrolled = remember(offset) { offset < -20f }

    val backgroundColor = MaterialTheme.colorScheme.background
    val glassColor = MaterialTheme.colorScheme.surfaceContainer.copy(0.5f)

    val targetColor by animateColorAsState(
        targetValue = if (isScrolled) glassColor else backgroundColor,
        animationSpec = tween(durationMillis = 400),
        label = "topbar_color"
    )

    val targetAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 0.95f else 1.1f,
        animationSpec = tween(durationMillis = 400),
        label = "topbar_alpha"
    )

    val targetBlur by animateDpAsState(
        targetValue = if (isScrolled) 12.dp else 0.dp,
        animationSpec = tween(durationMillis = 400),
        label = "topbar_blur"
    )

    val context = LocalContext.current
    val profilePicturePrefs = remember(context) { ProfilePicturePrefs(context) }
    val savedUri by profilePicturePrefs.getProfilePicture().collectAsState(initial = null)
    var profileImageUri by remember(savedUri) { mutableStateOf(savedUri) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(targetBlur)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            targetColor.copy(alpha = targetAlpha * 0.85f),
                            targetColor.copy(alpha = targetAlpha * 0.95f),
                            targetColor.copy(alpha = targetAlpha)
                        )
                    )
                )
        )
        TopAppBar(
            title = {
                if (isInMainContent) {
                    Text(text = currentContentType)
                }
            },
            navigationIcon = {
                if (isInMainContent) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = EvaIcons.Outline.ArrowIosBack,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            actions = {
                if (!isInMainContent) {
                    IconButton(onClick = onProfileSettingsClick) {
                        PhotoProfile(
                            imageUri = profileImageUri
                        )
                        /*                        Icon(
                                                    imageVector = Icons.Rounded.AccountCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.fillMaxSize(),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )*/
                    }
                    Text(
                        text = "•",
                        fontSize = 24.sp,
                        color = Color.Green.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(start = 2.dp, end = 16.dp)
                    )
                } else {
                    Text(
                        text = "•",
                        fontSize = 24.sp,
                        color = Color.Green.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TripkeunTopBarPreview() {
    ArkheTheme {
        ArkheTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            isInMainContent = true,
            currentContentType = stringResource(R.string.docs),
            onBackClick = {},
            onProfileSettingsClick = {}
        )
    }
}