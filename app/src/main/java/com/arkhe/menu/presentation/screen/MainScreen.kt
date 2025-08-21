@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.arkhe.menu.presentation.components.MainContent
import com.arkhe.menu.presentation.components.ProfileBottomSheet
import com.arkhe.menu.presentation.components.TripkeunBottomBar
import com.arkhe.menu.presentation.components.TripkeunTopBar
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showProfileBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleProfileBottomSheet() }
        ) {
            ProfileBottomSheet()
        }
    }

    Scaffold(
        topBar = {
            TripkeunTopBar(
                isInMainContent = uiState.isInMainContent,
                currentContentType = uiState.currentContentType,
                onBackClick = { viewModel.navigateBack() },
                onUserIconClick = { viewModel.toggleProfileBottomSheet() }
            )
        },
        bottomBar = {
            if (uiState.showBottomBar) {
                TripkeunBottomBar(
                    selectedItem = uiState.selectedBottomNavItem,
                    onItemSelected = { viewModel.selectBottomNavItem(it) }
                )
            }
        }
    ) { paddingValues ->
        MainContent(
            modifier = Modifier.padding(paddingValues),
            selectedBottomNavItem = uiState.selectedBottomNavItem,
            userRole = uiState.userRole,
            isInMainContent = uiState.isInMainContent,
            onNavigateToContent = { contentType ->
                viewModel.navigateToMainContent(contentType)
            }
        )

        // Loading overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LoadingIndicator()
            }
        }

        // Error handling
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar or error dialog
                viewModel.setError(null)
            }
        }
    }
}