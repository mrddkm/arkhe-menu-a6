package com.arkhe.menu.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.auth.OnboardingScreen
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoryDetail
import com.arkhe.menu.presentation.screen.docs.product.detail.ProductDetailScreen
import com.arkhe.menu.presentation.screen.settings.about.AboutScreen
import com.arkhe.menu.presentation.screen.settings.account.PersonalInfoScreen
import com.arkhe.menu.presentation.screen.settings.account.SignInSecurityScreen
import com.arkhe.menu.presentation.screen.settings.devices.DevicesScreen
import com.arkhe.menu.presentation.screen.settings.privacy.PrivacyScreen
import com.arkhe.menu.presentation.screen.settings.settings.SettingsBottomSheet
import com.arkhe.menu.presentation.screen.settings.terms.TermsScreen
import com.arkhe.menu.presentation.ui.components.LoadingOverlay
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.MainViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.samplePasswordData
import com.arkhe.menu.utils.samplePinData
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Lock
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArkheNavigation(
    navController: NavHostController = rememberNavController()
) {
    val mainViewModel: MainViewModel = koinViewModel()
    val uiState by mainViewModel.uiState.collectAsState()
    val langViewModel: LanguageViewModel = koinViewModel()
    val authViewModel: AuthViewModel = koinViewModel()
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.ON_BOARDING,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            /*OnBoarding*/
            composable(NavigationRoute.ON_BOARDING) {
                OnboardingScreen(
                    navController = navController,
                    onNavigateToMain = {
                        scope.launch {
                            mainViewModel.showLoadingOverlay()
                            coroutineScope {
                                launch { delay(1000L) }
                            }
                            mainViewModel.hideLoadingOverlay()
                            navController.navigate(NavigationRoute.MAIN) {
                                popUpTo(NavigationRoute.ON_BOARDING) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            /*Main*/
            composable(NavigationRoute.MAIN) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            /*Docs*/
            composable(NavigationRoute.PROFILE) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            composable(NavigationRoute.ORGANIZATION) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            composable(NavigationRoute.CUSTOMER) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            composable(NavigationRoute.CATEGORIES) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            composable(NavigationRoute.CATEGORY_DETAIL) {
                CategoryDetail(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(NavigationRoute.PRODUCTS) {
                MainScreen(
                    navController = navController,
                    onProfileSettingsClick = { mainViewModel.toggleProfileSettingsBottomSheet() }
                )
            }

            composable(
                route = NavigationRoute.PRODUCT_DETAIL,
                arguments = listOf(
                    navArgument("productId") {
                        type = NavType.StringType
                        nullable = false
                    },
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val productViewModel: ProductViewModel =
                    koinViewModel(key = "main_product_viewmodel")
                ProductDetailScreen(
                    productId = productId,
                    navController = navController,
                    productViewModel = productViewModel,
                    onBackClick = {
                        val popSuccess = navController.popBackStack()
                        if (!popSuccess) {
                            navController.navigate(NavigationRoute.MAIN) {
                                popUpTo(NavigationRoute.MAIN) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            /*tripkeun*/

            /*Activity*/

            /*Profile & Settings*/
            composable(
                route = NavigationRoute.PERSONAL_INFO_DETAIL,
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                PersonalInfoScreen(
                    onBackClick = {
                        scope.launch {
                            mainViewModel.showProfileSettingsBottomSheet()
                            delay(50L)
                            val popSuccess = navController.popBackStack()
                            if (!popSuccess) {
                                navController.navigate(NavigationRoute.MAIN) {
                                    popUpTo(NavigationRoute.MAIN) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    user = sampleUser,
                    onUserUpdate = {}
                )
            }

            composable(
                route = NavigationRoute.SIGN_IN_SECURITY_DETAIL,
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                SignInSecurityScreen(
                    onBackClick = {
                        scope.launch {
                            mainViewModel.showProfileSettingsBottomSheet()
                            delay(50L)
                            val popSuccess = navController.popBackStack()
                            if (!popSuccess) {
                                navController.navigate(NavigationRoute.MAIN) {
                                    popUpTo(NavigationRoute.MAIN) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    onSignedOutScreenClick = {
                        scope.launch {
                            mainViewModel.showLoadingOverlay()
                            coroutineScope {
                                launch { authViewModel.signedOutAuthState() }
                                launch { delay(1000L) }
                            }
                            mainViewModel.hideLoadingOverlay()
                            navController.navigate(NavigationRoute.ON_BOARDING) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    user = sampleUser,
                    passwordData = samplePasswordData,
                    onUserUpdate = {},
                    onPasswordUpdate = {}
                )
            }

            composable(
                route = NavigationRoute.DEVICES_DETAIL,
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                DevicesScreen(
                    onBackClick = {
                        scope.launch {
                            mainViewModel.showProfileSettingsBottomSheet()
                            delay(50L)
                            val popSuccess = navController.popBackStack()
                            if (!popSuccess) {
                                navController.navigate(NavigationRoute.MAIN) {
                                    popUpTo(NavigationRoute.MAIN) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    },
                    onDeactivationClick = {
                        scope.launch {
                            mainViewModel.showLoadingOverlay()
                            coroutineScope {
                                launch { authViewModel.deactivatedAuthState() }
                                launch { delay(1000L) }
                            }
                            mainViewModel.hideLoadingOverlay()
                            navController.navigate(NavigationRoute.ON_BOARDING) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    user = sampleUser,
                    onUserUpdate = {},
                    pinData = samplePinData,
                    onPinUpdate = {}
                )
            }

            composable(
                route = "${NavigationRoute.ABOUT_DETAIL}/{source}",
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: "unknown"
                AboutScreen(
                    onBackClick = {
                        when (source) {
                            NavigationRoute.MAIN -> {
                                scope.launch {
                                    mainViewModel.showProfileSettingsBottomSheet()
                                    delay(50L)
                                    val popSuccess = navController.popBackStack()
                                    if (!popSuccess) {
                                        navController.navigate(NavigationRoute.MAIN) {
                                            popUpTo(NavigationRoute.MAIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            }

                            NavigationRoute.ON_BOARDING -> navController.popBackStack()

                            else -> navController.popBackStack()
                        }
                    }
                )
            }

            composable(
                route = "${NavigationRoute.PRIVACY_POLICY_DETAIL}/{source}",
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: "unknown"
                PrivacyScreen(
                    onBackClick = {
                        when (source) {
                            NavigationRoute.MAIN -> {
                                scope.launch {
                                    mainViewModel.showProfileSettingsBottomSheet()
                                    delay(50L)
                                    val popSuccess = navController.popBackStack()
                                    if (!popSuccess) {
                                        navController.navigate(NavigationRoute.MAIN) {
                                            popUpTo(NavigationRoute.MAIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            }

                            NavigationRoute.ON_BOARDING -> navController.popBackStack()

                            else -> navController.popBackStack()
                        }
                    }
                )
            }

            composable(
                route = "${NavigationRoute.TERMS_OF_SERVICE_DETAIL}/{source}",
                arguments = listOf(
                    navArgument("source") {
                        type = NavType.StringType
                        nullable = false
                        defaultValue = "unknown"
                    }
                )
            ) { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: "unknown"
                TermsScreen(
                    onBackClick = {
                        when (source) {
                            NavigationRoute.MAIN -> {
                                scope.launch {
                                    mainViewModel.showProfileSettingsBottomSheet()
                                    delay(50L)
                                    val popSuccess = navController.popBackStack()
                                    if (!popSuccess) {
                                        navController.navigate(NavigationRoute.MAIN) {
                                            popUpTo(NavigationRoute.MAIN) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            }

                            NavigationRoute.ON_BOARDING -> navController.popBackStack()

                            else -> navController.popBackStack()
                        }
                    }
                )
            }
        }

        /*Loading Overlay - appear above all content*/
        LoadingOverlay(
            isVisible = uiState.isLoadingOverlay,
            isDefaultIcon = true,
            pleaseWaitText = langViewModel.getLocalized(Lang.PLEASE_WAIT)
        )

        /*--- Setting & Profile BottomSheet ---*/
        if (uiState.showProfileSettingsBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { },
                sheetState = sheetState,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                            .padding(top = 8.dp)
                            .align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = EvaIcons.Fill.Lock,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            ) {
                SettingsBottomSheet(
                    langViewModel = langViewModel,
                    mainViewModel = mainViewModel,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                        }
                    },
                    onLockScreenClick = {
                        scope.launch {
                            mainViewModel.showLoadingOverlay()
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            coroutineScope {
                                delay(500L)
                            }
                            mainViewModel.hideLoadingOverlay()
                            navController.navigate(route = NavigationRoute.ON_BOARDING) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onPersonalInfoClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                route = NavigationRoute.personalInfoDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    },
                    onSignInSecurityClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                NavigationRoute.signInSecurityDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    },
                    onDevicesClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                NavigationRoute.devicesDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    },
                    onAboutClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                NavigationRoute.aboutDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    },
                    onPrivacyPolicyClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                NavigationRoute.privacyPolicyDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    },
                    onTermsOfServiceClick = {
                        scope.launch {
                            sheetState.hide()
                            mainViewModel.toggleProfileSettingsBottomSheet()
                            navController.navigate(
                                NavigationRoute.termOfServiceDetail(source = NavigationRoute.MAIN)
                            )
                        }
                    }
                )
            }
        }
    }
}