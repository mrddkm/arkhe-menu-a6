package com.arkhe.menu.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.presentation.viewmodel.BottomNavItem

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    selectedBottomNavItem: BottomNavItem,
    userRole: UserRole,
    isInMainContent: Boolean,
    onNavigateToContent: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        when (selectedBottomNavItem) {
            BottomNavItem.DOCS -> {
                DocsContent(onNavigateToContent = onNavigateToContent)
            }
            BottomNavItem.TRIPKEUN -> {
                TripkeunContent(onNavigateToContent = onNavigateToContent)
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
