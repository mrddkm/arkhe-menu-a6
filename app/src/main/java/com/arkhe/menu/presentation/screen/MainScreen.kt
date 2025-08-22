@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.presentation.components.MainContent
import com.arkhe.menu.presentation.components.ProfileBottomSheet
import com.arkhe.menu.presentation.components.TripkeunBottomBar
import com.arkhe.menu.presentation.components.TripkeunTopBar
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
//    viewModel: MainViewModel = koinViewModel()
) {
    val vm = koinViewModel<MainViewModel>()

    val uiState by vm.uiState.collectAsState()

    if (uiState.showProfileBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { vm.toggleProfileBottomSheet() }
        ) {
            ProfileBottomSheet()
        }
    }

    Scaffold(
        topBar = {
            TripkeunTopBar(
                isInMainContent = uiState.isInMainContent,
                currentContentType = uiState.currentContentType,
                onBackClick = { vm.navigateBack() },
                onUserIconClick = { vm.toggleProfileBottomSheet() }
            )
        },
        bottomBar = {
            if (uiState.showBottomBar) {
                TripkeunBottomBar(
                    selectedItem = uiState.selectedBottomNavItem,
                    onItemSelected = { vm.selectBottomNavItem(it) }
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
                vm.navigateToMainContent(contentType)
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
                vm.setError(null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current // Get context here
    KoinApplicationPreview(
        application = {
            androidContext(previewContext) // Pass the context
            modules(
                appModule, dataModule, domainModule
            )
        }
    ) {
        AppTheme {
            MainScreen(navController = androidx.navigation.compose.rememberNavController())
        }
    }
}