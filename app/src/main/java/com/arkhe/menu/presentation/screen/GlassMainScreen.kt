@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GroupWork
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.presentation.animation.ScreenTransitions
import com.arkhe.menu.presentation.components.UserBottomSheet
import com.arkhe.menu.presentation.components.common.LoadingIndicatorSpinner
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.docs.categories.CategoriesScreen
import com.arkhe.menu.presentation.screen.docs.customer.CustomerScreen
import com.arkhe.menu.presentation.screen.docs.organization.OrganizationScreen
import com.arkhe.menu.presentation.screen.docs.product.ProductsScreen
import com.arkhe.menu.presentation.screen.docs.profile.ProfileScreen
import com.arkhe.menu.presentation.ui.components.BottomNavItem as GlassNavItem
import com.arkhe.menu.presentation.ui.components.GlassBottomBar
import com.arkhe.menu.presentation.ui.components.GlassTopBar
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.BottomNavItem
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Activity
import compose.icons.evaicons.outline.Grid
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassMainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            viewModel.updateNavigationState(destination.route)
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

    if (uiState.showProfileBottomSheet) {
        ModalBottomSheet(onDismissRequest = { viewModel.toggleProfileBottomSheet() }) {
            UserBottomSheet()
        }
    }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            GlassTopBar(
                title = uiState.currentContentType.ifEmpty { "" },
                listState = listState,
                isInMainContent = uiState.isInMainContent,
                previousLabel = if (!uiState.isInMainContent) {
                    uiState.selectedBottomNavItem.title
                } else null,
                onBackClick = {
                    if (!uiState.isInMainContent) {
                        viewModel.navigateBackToMain()
                    }
                },
                onProfileClick = {
                    if (uiState.isInMainContent) {
                        viewModel.toggleProfileBottomSheet()
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.showBottomBar) {
                GlassBottomBar(
                    items = BottomNavItem.entries.map {
                        GlassNavItem(label = it.title, icon = getIconForItem(it))
                    },
                    selectedIndex = BottomNavItem.entries.indexOf(uiState.selectedBottomNavItem),
                    onItemSelected = { index ->
                        viewModel.selectBottomNavItem(BottomNavItem.entries[index])
                    }
                )
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = { ScreenTransitions.crossFade() },
            modifier = Modifier.fillMaxSize(),
            label = "glass_screen_transition"
        ) { currentScreen ->
            when (currentScreen) {
                NavigationRoute.PROFILE -> {
                    ProfileScreen(modifier = Modifier.padding(paddingValues))
                }
                NavigationRoute.ORGANIZATION -> {
                    OrganizationScreen(modifier = Modifier.padding(paddingValues))
                }
                NavigationRoute.CUSTOMER -> {
                    CustomerScreen(modifier = Modifier.padding(paddingValues))
                }
                NavigationRoute.CATEGORIES -> {
                    CategoriesScreen(
                        modifier = Modifier.padding(paddingValues),
                        onNavigateToDetail = {
                            navController.navigate(NavigationRoute.categoryDetail())
                        }
                    )
                }
                NavigationRoute.PRODUCTS -> {
                    ProductsScreen(modifier = Modifier.padding(paddingValues))
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
                        onNavigateToProfile = { viewModel.navigateToProfile() },
                        onNavigateToOrganization = { viewModel.navigateToOrganization() },
                        onNavigateToCustomer = { viewModel.navigateToCustomer() },
                        onNavigateToCategories = { viewModel.navigateToCategory() },
                        onNavigateToProducts = { viewModel.navigateToProducts() }
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                LoadingIndicatorSpinner()
            }
        }

        uiState.error?.let { error ->
            LaunchedEffect(error) { viewModel.setError(null) }
        }
    }
}

private fun getIconForItem(item: BottomNavItem) = when (item) {
    BottomNavItem.DOCS -> EvaIcons.Outline.Grid
    BottomNavItem.TRIPKEUN -> Icons.Rounded.GroupWork
    BottomNavItem.ACTIVITY -> EvaIcons.Outline.Activity
}

@Preview(showBackground = true)
@Composable
fun GlassMainScreenPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule
            )
        }
    ) {
        AppTheme {
            GlassMainScreen(navController = rememberNavController())
        }
    }
}