@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.activity.faga.receipt.CreateReceiptScreen
import com.arkhe.menu.presentation.screen.activity.faga.receipt.ReceiptListScreen

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

        composable(NavigationRoute.PRODUCTS) {
            MainScreen(navController = navController)
        }

        /*tripkeun*/

        /*Activity*/
        composable(NavigationRoute.CREATE_RECEIPT) {
            CreateReceiptScreen(navController = navController)
        }

        composable(NavigationRoute.RECEIPT_LIST) {
            ReceiptListScreen(navController = navController)
        }
    }
}