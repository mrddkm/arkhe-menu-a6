// NavigationGraph.kt
package com.arkhe.menu.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.arkhe.menu.presentation.screen.MainScreen
import com.arkhe.menu.presentation.screen.activity.faga.receipt.CreateReceiptScreen
import com.arkhe.menu.presentation.screen.activity.faga.receipt.ReceiptListScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.MAIN
    ) {
        composable(NavigationRoute.MAIN) {
            MainScreen(navController = navController)
        }

        composable(NavigationRoute.CREATE_RECEIPT) {
            CreateReceiptScreen(navController = navController)
        }

        composable(NavigationRoute.RECEIPT_LIST) {
            ReceiptListScreen(navController = navController)
        }
    }
}