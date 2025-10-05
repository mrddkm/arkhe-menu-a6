package com.arkhe.menu.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
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

private const val TAG = "ArkheNavigation"

@Composable
fun ArkheNavigation(
    navController: NavHostController = rememberNavController()
) {
    var lastMainRoute by remember { mutableStateOf(NavigationRoute.MAIN) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            Log.d(TAG, "🧭 Navigation: $route")

            if (route != null && !route.startsWith("product_detail") && !route.startsWith("category_detail")) {
                lastMainRoute = route
                Log.d(TAG, "   - Saved lastMainRoute: $lastMainRoute")
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

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

            val productViewModel: ProductViewModel = koinViewModel(key = "main_product_viewmodel")

            ProductDetailScreen(
                productId = productId,
                source = source,
                navController = navController,
                productViewModel = productViewModel,
                onBackClick = {
                    Log.d(TAG, "🔙 ProductDetail onBackClick")
                    Log.d(TAG, "   - source: $source")
                    Log.d(TAG, "   - lastMainRoute: $lastMainRoute")

                    val popSuccess = navController.popBackStack()

                    Log.d(TAG, "   - popSuccess: $popSuccess")

                    if (!popSuccess) {
                        Log.w(TAG, "   - popBackStack failed, navigating to MAIN as fallback")
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