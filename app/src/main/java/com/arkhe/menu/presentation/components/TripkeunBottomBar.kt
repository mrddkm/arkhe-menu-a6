@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkhe.menu.presentation.viewmodel.BottomNavItem

@Composable
fun TripkeunBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = getIconForItem(item),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

private fun getIconForItem(item: BottomNavItem): ImageVector {
    return when (item) {
        BottomNavItem.DOCS -> Icons.Default.Description
        BottomNavItem.TRIPKEUN -> Icons.Default.Map
        BottomNavItem.ACTIVITY -> Icons.Default.Event
    }
}