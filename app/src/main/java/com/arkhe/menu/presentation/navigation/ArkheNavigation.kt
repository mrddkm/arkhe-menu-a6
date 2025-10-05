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
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

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
    }
}