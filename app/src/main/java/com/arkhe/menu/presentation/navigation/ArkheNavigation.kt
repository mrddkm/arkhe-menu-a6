@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arkhe.menu.presentation.screen.GlassMainScreen
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoryDetail

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
            GlassMainScreen(navController = navController)
        }

        /*Docs*/
        composable(NavigationRoute.PROFILE) {
            GlassMainScreen(navController = navController)
        }

        composable(NavigationRoute.ORGANIZATION) {
            GlassMainScreen(navController = navController)
        }

        composable(NavigationRoute.CUSTOMER) {
            GlassMainScreen(navController = navController)
        }

        composable(NavigationRoute.CATEGORIES) {
            GlassMainScreen(navController = navController)
        }

        composable(NavigationRoute.CATEGORY_DETAIL) {
            CategoryDetail(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavigationRoute.PRODUCTS) {
            GlassMainScreen(navController = navController)
        }

        /*tripkeun*/

        /*Activity*/
    }
}