package com.arkhe.menu.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.AppTheme
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
    onUserIconClick: () -> Unit,
    alwaysShowWhenInContent: Boolean = true
) {
    val overlap = when {
        isInMainContent && alwaysShowWhenInContent -> {
            maxOf(0.3f, scrollBehavior.state.overlappedFraction.coerceIn(0f, 1f))
        }

        else -> {
            scrollBehavior.state.overlappedFraction.coerceIn(0f, 1f)
        }
    }

    val targetBlur = if (isInMainContent && alwaysShowWhenInContent) {
        (8f * (overlap - 0.3f).coerceAtLeast(0f) / 0.7f).dp
    } else {
        (16f * overlap).dp
    }
    val animatedBlur by animateDpAsState(targetValue = targetBlur)

    val targetAlpha = if (isInMainContent && alwaysShowWhenInContent) {
        0.25f + (0.65f * overlap)
    } else {
        0.8f * overlap
    }
    val animatedAlpha by animateFloatAsState(targetValue = targetAlpha)

    TopAppBar(
        title = {
            if (isInMainContent) {
                Text(text = currentContentType)
            }
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (isInMainContent) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = EvaIcons.Outline.ArrowIosBack,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        actions = {
            if (!isInMainContent) {
                IconButton(onClick = onUserIconClick) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = animatedAlpha),
                        Color.Transparent
                    )
                )
            )
            .then(if (animatedBlur > 0.dp) Modifier.blur(animatedBlur) else Modifier)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TripkeunTopBarPreview() {
    AppTheme {
        ArkheTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            isInMainContent = false,
            currentContentType = "Home",
            onBackClick = {},
            onUserIconClick = {}
        )
    }
}