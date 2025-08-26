@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.animation.AnimatedContent
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
import com.arkhe.menu.presentation.animation.ScreenTransitions
import com.arkhe.menu.presentation.components.UserBottomSheet
import com.arkhe.menu.presentation.components.ArkheBottomBar
import com.arkhe.menu.presentation.components.ArkheTopBar
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.docs.category.CategoriesScreen
import com.arkhe.menu.presentation.screen.docs.customer.CustomerScreen
import com.arkhe.menu.presentation.screen.docs.organization.OrganizationScreen
import com.arkhe.menu.presentation.screen.docs.product.ProductsScreen
import com.arkhe.menu.presentation.screen.docs.profile.ProfileScreen
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
            UserBottomSheet()
        }
    }

    Scaffold(
        topBar = {
            ArkheTopBar(
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
                ArkheBottomBar(
                    selectedItem = uiState.selectedBottomNavItem,
                    onItemSelected = { viewModel.selectBottomNavItem(it) }
                )
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = {
                when {
                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.PROFILE -> {
                        ScreenTransitions.slideFromLeft()
                    }

                    initialState == NavigationRoute.PROFILE && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.slideFromRight()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.ORGANIZATION -> {
                        ScreenTransitions.slideFromLeft()
                    }

                    initialState == NavigationRoute.ORGANIZATION && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.slideFromRight()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.CUSTOMER -> {
                        ScreenTransitions.slideFromLeft()
                    }

                    initialState == NavigationRoute.CUSTOMER && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.slideFromRight()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.CATEGORIES -> {
                        ScreenTransitions.slideFromLeft()
                    }

                    initialState == NavigationRoute.CATEGORIES && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.slideFromRight()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.PRODUCTS -> {
                        ScreenTransitions.slideFromLeft()
                    }

                    initialState == NavigationRoute.PRODUCTS && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.slideFromRight()
                    }

                    else -> {
                        ScreenTransitions.crossFade()
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            label = "screen_transition"
        ) { currentScreen ->
            when (currentScreen) {
                NavigationRoute.PROFILE -> {
                    ProfileScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                NavigationRoute.ORGANIZATION -> {
                    OrganizationScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                NavigationRoute.CUSTOMER -> {
                    CustomerScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                NavigationRoute.CATEGORIES -> {
                    CategoriesScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                NavigationRoute.PRODUCTS -> {
                    ProductsScreen(
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
                        },
                        onNavigateToOrganization = {
                            viewModel.navigateToOrganization()
                        },
                        onNavigateToCustomer = {
                            viewModel.navigateToCustomer()
                        },
                        onNavigateToCategories = {
                            viewModel.navigateToCategory()
                        },
                        onNavigateToProducts = {
                            viewModel.navigateToProducts()
                        }
                    )
                }
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
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
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