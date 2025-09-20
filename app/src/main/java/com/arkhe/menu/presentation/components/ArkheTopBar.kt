@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkhe.menu.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArkheTopBar(
    isInMainContent: Boolean,
    currentContentType: String,
    onBackClick: () -> Unit,
    onUserIconClick: () -> Unit
) {
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
                        imageVector = Icons.Rounded.ChevronLeft,
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview
@Composable
fun TripkeunTopBarPreview() {
    AppTheme {
        ArkheTopBar(
            isInMainContent = false,
            currentContentType = "Home",
            onBackClick = {},
            onUserIconClick = {}
        )
    }
}