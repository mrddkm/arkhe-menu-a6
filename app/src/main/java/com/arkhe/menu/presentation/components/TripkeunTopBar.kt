@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripkeunTopBar(
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (!isInMainContent) {
                IconButton(onClick = onUserIconClick) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Profile"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}