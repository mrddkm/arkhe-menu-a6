@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.docs.categories.CategoriesScreen
import com.arkhe.menu.presentation.screen.docs.customer.CustomerScreen
import com.arkhe.menu.presentation.screen.docs.organization.OrganizationScreen
import com.arkhe.menu.presentation.screen.docs.product.ProductsScreen
import com.arkhe.menu.presentation.screen.docs.profile.ProfileScreen
import com.arkhe.menu.presentation.screen.user.UserBottomSheet
import com.arkhe.menu.presentation.ui.animation.ScreenTransitions
import com.arkhe.menu.presentation.ui.components.ArkheBottomBar
import com.arkhe.menu.presentation.ui.components.ArkheTopBar
import com.arkhe.menu.presentation.ui.components.LoadingIndicatorSpinner
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
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
    val scrollAlpha by viewModel.scrollAlpha.collectAsState()

    val productViewModel: ProductViewModel = koinViewModel(key = "main_product_viewmodel")

    var topBarHeightPx by remember { mutableIntStateOf(0) }
    var bottomBarHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val topBarHeight = with(density) { topBarHeightPx.toDp() }
    val bottomBarHeight = with(density) { bottomBarHeightPx.toDp() }

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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = {
                when {
                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.PROFILE -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.PROFILE && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.ORGANIZATION -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.ORGANIZATION && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.CUSTOMER -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.CUSTOMER && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.CATEGORIES -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.CATEGORIES && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.CATEGORIES && targetState.startsWith("category_detail") -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState.startsWith("category_detail") && targetState == NavigationRoute.CATEGORIES -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.MAIN && targetState == NavigationRoute.PRODUCTS -> {
                        ScreenTransitions.crossFade()
                    }

                    initialState == NavigationRoute.PRODUCTS && targetState == NavigationRoute.MAIN -> {
                        ScreenTransitions.crossFade()
                    }

                    else -> {
                        ScreenTransitions.noAnimation()
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            label = "screen_transition"
        ) { currentScreen ->
            when (currentScreen) {
                NavigationRoute.PROFILE -> {
                    ProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        topBarHeight = topBarHeight
                    )
                }

                NavigationRoute.ORGANIZATION -> {
                    OrganizationScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                NavigationRoute.CUSTOMER -> {
                    CustomerScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                NavigationRoute.CATEGORIES -> {
                    CategoriesScreen(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        onNavigateToDetail = {
                            navController.navigate(NavigationRoute.categoryDetail())
                        },
                        topBarHeight = topBarHeight
                    )
                }

                NavigationRoute.PRODUCTS -> {
                    ProductsScreen(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        productViewModel = productViewModel,
                        topBarHeight = topBarHeight
                    )
                }

                else -> {
                    MainContent(
                        modifier = Modifier.fillMaxSize(),
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
                        onNavigateToProducts = { viewModel.navigateToProducts() },
                        onScrollAlphaChange = { alpha ->
                            viewModel.updateScrollAlpha(alpha)
                        },
                        topBarHeight = topBarHeight,
                        bottomBarHeight = bottomBarHeight,
                        scrollBehavior = scrollBehavior
                    )
                }
            }
        }

        /*--- Floating TopBar ---*/
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .onGloballyPositioned { coords ->
                    topBarHeightPx = coords.size.height
                }
        ) {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 12.dp,
                shape = MaterialTheme.shapes.large,
                color = Color.Transparent
            ) {
                ArkheTopBar(
                    scrollBehavior = scrollBehavior,
                    isInMainContent = uiState.isInMainContent,
                    currentContentType = uiState.currentContentType,
                    onBackClick = { viewModel.navigateBackToMain() },
                    onUserIconClick = { viewModel.toggleProfileBottomSheet() }
                )
            }
        }

        /*--- Floating BottomBar ---*/
        if (uiState.showBottomBar) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .align(Alignment.BottomCenter)
                    .onGloballyPositioned { coords ->
                        bottomBarHeightPx = coords.size.height
                    }
            ) {
                Surface(
                    modifier = Modifier
                        .height(68.dp),
                    tonalElevation = 6.dp,
                    shadowElevation = 12.dp,
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ) {
                    ArkheBottomBar(
                        selectedItem = uiState.selectedBottomNavItem,
                        onItemSelected = { viewModel.selectBottomNavItem(it) },
                        scrollAlpha = scrollAlpha
                    )
                }
            }
        }

        /*--- Loading overlay ---*/
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicatorSpinner()
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
                dataModule,
                domainModule,
                appModule
            )
        }
    ) {
        AppTheme {
            MainScreen(navController = rememberNavController())
        }
    }
}