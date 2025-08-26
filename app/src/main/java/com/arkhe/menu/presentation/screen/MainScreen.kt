@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.presentation.components.ProfileBottomSheet
import com.arkhe.menu.presentation.components.TripkeunBottomBar
import com.arkhe.menu.presentation.components.TripkeunTopBar
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.screen.docs.profile.ProfileTripkeunScreen
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            viewModel.updateNavigationState(destination.route)
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

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
                onBackClick = {
                    viewModel.navigateBackToMain()
                },
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
        when (uiState.currentScreen) {
            "PROFILE_TRIPKEUN" -> {
                ProfileTripkeunScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                MainContent(
                    modifier = Modifier.padding(paddingValues),
                    selectedBottomNavItem = uiState.selectedBottomNavItem,
                    userRole = uiState.userRole,
                    navController = navController,
                    onNavigateToContent = { contentType ->
                        viewModel.navigateToMainContent(contentType)
                    },
                    onNavigateToProfile = {
                        viewModel.navigateToProfile()
                    }
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LoadingIndicator()
            }
        }

        uiState.error?.let { error ->
            LaunchedEffect(error) {
                viewModel.setError(null)
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

/*
@Preview(showBackground = true)
@Composable
fun MainScreenDebugPreview() {
    AppTheme {
        // Preview sederhana tanpa dependencies
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Main Screen Preview",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Preview mode active",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
*/
