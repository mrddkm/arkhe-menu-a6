package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.auth.OnboardingScreen
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoryDetail
import com.arkhe.menu.presentation.screen.docs.product.detail.ProductDetailScreen
import com.arkhe.menu.presentation.screen.settings.about.AboutScreen
import com.arkhe.menu.presentation.screen.settings.account.PersonalInfoScreen
import com.arkhe.menu.presentation.screen.settings.account.SignInSecurityScreen
import com.arkhe.menu.presentation.screen.settings.devices.DevicesScreen
import com.arkhe.menu.presentation.screen.settings.privacy.PrivacyScreen
import com.arkhe.menu.presentation.screen.settings.terms.TermsScreen
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.samplePasswordData
import com.arkhe.menu.utils.samplePinData
import com.arkhe.menu.utils.sampleUser
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArkheNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.ON_BOARDING
    ) {
        /*OnBoarding*/
        composable(NavigationRoute.ON_BOARDING) {
            OnboardingScreen(
                navController = navController,
                onNavigateToMain = {
                    navController.navigate(NavigationRoute.MAIN) {
                        popUpTo(NavigationRoute.ON_BOARDING) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*Main*/
        composable(NavigationRoute.MAIN) {
            MainScreen(navController = navController)
        }

        /*Docs*/
        composable(NavigationRoute.PROFILE) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.ORGANIZATION) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.CUSTOMER) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.CATEGORIES) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.CATEGORY_DETAIL) {
            CategoryDetail(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavigationRoute.PRODUCTS) {
            MainScreen(navController = navController)
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
            val productViewModel: ProductViewModel = koinViewModel(key = "main_product_viewmodel")
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
                    val popSuccess = navController.popBackStack()
                    if (!popSuccess) {
                        navController.navigate(NavigationRoute.MAIN) {
                            popUpTo(NavigationRoute.MAIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                navController = navController,
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
                    val popSuccess = navController.popBackStack()
                    if (!popSuccess) {
                        navController.navigate(NavigationRoute.MAIN) {
                            popUpTo(NavigationRoute.MAIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                navController = navController,
                user = sampleUser,
                passwordData = samplePasswordData,
                pinData = samplePinData,
                onUserUpdate = {},
                onPasswordUpdate = {},
                onPinUpdate = {}
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
                    val popSuccess = navController.popBackStack()
                    if (!popSuccess) {
                        navController.navigate(NavigationRoute.MAIN) {
                            popUpTo(NavigationRoute.MAIN) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                navController = navController
            )
        }

        composable(
            route = NavigationRoute.ABOUT_DETAIL,
            arguments = listOf(
                navArgument("source") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = "unknown"
                }
            )
        ) { backStackEntry ->
            AboutScreen(
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
                },
                navController = navController
            )
        }

        composable(
            route = NavigationRoute.PRIVACY_POLICY_DETAIL,
            arguments = listOf(
                navArgument("source") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = "unknown"
                }
            )
        ) { backStackEntry ->
            PrivacyScreen(
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
                },
                navController = navController
            )
        }

        composable(
            route = NavigationRoute.TERMS_OF_SERVICE_DETAIL,
            arguments = listOf(
                navArgument("source") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = "unknown"
                }
            )
        ) { backStackEntry ->
            TermsScreen(
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
                },
                navController = navController
            )
        }
    }
}