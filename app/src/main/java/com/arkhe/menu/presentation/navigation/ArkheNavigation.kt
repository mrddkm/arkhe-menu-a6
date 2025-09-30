package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoryDetail
import com.arkhe.menu.presentation.screen.docs.product.detail.ProductDetailScreen

@Composable
fun ArkheNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MAIN
    ) {
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
            val source = backStackEntry.arguments?.getString("source") ?: "unknown"

            ProductDetailScreen(
                productId = productId,
                source = source,
                navController = navController,
                onBackClick = {
                    val popSuccess = when (source) {
                        NavigationRoute.PRODUCTS -> {
                            navController.popBackStack(NavigationRoute.PRODUCTS, false)
                        }

                        NavigationRoute.DOCS -> {
                            navController.popBackStack(NavigationRoute.MAIN, false)
                        }

                        NavigationRoute.CATEGORIES -> {
                            navController.popBackStack(NavigationRoute.CATEGORIES, false)
                        }

                        else -> {
                            navController.popBackStack()
                        }
                    }

                    if (!popSuccess) {
                        when (source) {
                            NavigationRoute.PRODUCTS -> {
                                navController.navigate(NavigationRoute.PRODUCTS) {
                                    popUpTo(NavigationRoute.MAIN) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            NavigationRoute.DOCS -> {
                                navController.navigate(NavigationRoute.MAIN) {
                                    popUpTo(NavigationRoute.MAIN) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            NavigationRoute.CATEGORIES -> {
                                navController.navigate(NavigationRoute.CATEGORIES) {
                                    popUpTo(NavigationRoute.CATEGORIES) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }

                            else -> {
                                navController.navigate(NavigationRoute.MAIN) {
                                    popUpTo(0) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            )
        }

        /*tripkeun*/

        /*Activity*/
    }
}