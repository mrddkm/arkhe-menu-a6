package com.arkhe.menu.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.presentation.screen.activity.ActivityContent
import com.arkhe.menu.presentation.screen.docs.DocsContent
import com.arkhe.menu.presentation.screen.tripkeun.TripkeunContent
import com.arkhe.menu.presentation.viewmodel.BottomNavItem

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    selectedBottomNavItem: BottomNavItem,
    userRole: UserRole,
    navController: NavHostController,
    onNavigateToContent: (String) -> Unit,
    onNavigateToProfile: (() -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            when (selectedBottomNavItem) {
                BottomNavItem.DOCS -> {
                    DocsContent(
                        onNavigateToProfile = onNavigateToProfile ?: {}
                    )
                }

                BottomNavItem.TRIPKEUN -> {
                    TripkeunContent(
                        onNavigateToContent = onNavigateToContent
                    )
                }

                BottomNavItem.ACTIVITY -> {
                    ActivityContent(
                        userRole = userRole,
                        onNavigateToContent = onNavigateToContent
                    )
                }
            }
        }
    }
}
