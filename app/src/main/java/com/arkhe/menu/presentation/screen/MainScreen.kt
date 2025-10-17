@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.docs.categories.CategoriesScreen
import com.arkhe.menu.presentation.screen.docs.customer.CustomerScreen
import com.arkhe.menu.presentation.screen.docs.organization.OrganizationScreen
import com.arkhe.menu.presentation.screen.docs.product.ProductsScreen
import com.arkhe.menu.presentation.screen.docs.profile.ProfileScreen
import com.arkhe.menu.presentation.ui.animation.ScreenTransitions
import com.arkhe.menu.presentation.ui.components.ArkheBottomBar
import com.arkhe.menu.presentation.ui.components.ArkheTopBar
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onProfileSettingsClick: () -> Unit = {},
    mainViewModel: MainViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val isActivated by authViewModel.isActivatedFlow.collectAsState(initial = true)
    val isSignedIn by authViewModel.isSignedInFlow.collectAsState(initial = true)

    LaunchedEffect(isActivated, isSignedIn) {
        if (!isActivated && !isSignedIn) {
            navController.navigate(
                NavigationRoute.ON_BOARDING
            ) {
                popUpTo(NavigationRoute.MAIN) {
                    inclusive = true
                }
            }
        }
    }

    val uiState by mainViewModel.uiState.collectAsState()
    val scrollAlpha by mainViewModel.scrollAlpha.collectAsState()

    val productViewModel: ProductViewModel = koinViewModel(key = "main_product_viewmodel")

    var topBarHeightPx by remember { mutableIntStateOf(0) }
    var bottomBarHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val topBarHeight = with(density) { topBarHeightPx.toDp() }
    val bottomBarHeight = with(density) { bottomBarHeightPx.toDp() }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            mainViewModel.updateNavigationState(destination.route)
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    BackHandler(enabled = uiState.showProfileSettingsBottomSheet) {
        mainViewModel.toggleProfileSettingsBottomSheet()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                        ScreenTransitions.crossFade()
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            label = "screen_transition"
        ) { currentScreen ->
            when (currentScreen) {
                NavigationRoute.PROFILE -> {
                    ProfileScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.navigationBars)),
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
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.navigationBars)),
                        onNavigateToDetail = {
                            navController.navigate(NavigationRoute.categoryDetail())
                        },
                        topBarHeight = topBarHeight
                    )
                }

                NavigationRoute.PRODUCTS -> {
                    ProductsScreen(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.navigationBars)),
                        productViewModel = productViewModel,
                        topBarHeight = topBarHeight
                    )
                }

                else -> {
                    MainContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars.union(WindowInsets.navigationBars)),
                        selectedBottomNavItem = uiState.selectedBottomNavItem,
                        userRole = uiState.userRole,
                        navController = navController,
                        onNavigateToContent = { contentType ->
                            mainViewModel.navigateToMainContent(contentType)
                        },
                        onNavigateToProfile = { mainViewModel.navigateToProfile() },
                        onNavigateToOrganization = { mainViewModel.navigateToOrganization() },
                        onNavigateToCustomer = { mainViewModel.navigateToCustomer() },
                        onNavigateToCategories = { mainViewModel.navigateToCategory() },
                        onNavigateToProducts = { mainViewModel.navigateToProducts() },
                        onScrollAlphaChange = { alpha ->
                            mainViewModel.updateScrollAlpha(alpha)
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
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
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
                    onBackClick = { mainViewModel.navigateBackToMain() },
                    onProfileSettingsClick = onProfileSettingsClick
                )
            }
        }

        /*--- Floating BottomBar ---*/
        if (uiState.showBottomBar) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
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
                        onItemSelected = { mainViewModel.selectBottomNavItem(it) },
                        scrollAlpha = scrollAlpha
                    )
                }
            }
        }

        uiState.error?.let { error ->
            LaunchedEffect(error) {
                mainViewModel.setError(null)
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
                appModule,
                previewModule
            )
        }
    ) {
        ArkheTheme {
            MainScreen(
                navController = rememberNavController(),
                onProfileSettingsClick = {}
            )
        }
    }
}