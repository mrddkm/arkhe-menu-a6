@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.GroupWork
import androidx.compose.material.icons.rounded.WorkHistory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.BottomNavItem

@Composable
fun ArkheBottomBar(
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
        BottomNavItem.DOCS -> Icons.Rounded.Dashboard
        BottomNavItem.TRIPKEUN -> Icons.Rounded.GroupWork
        BottomNavItem.ACTIVITY -> Icons.Rounded.WorkHistory
    }
}

@Preview
@Composable
fun TripkeunBottomBarPreview() {
    AppTheme {
        ArkheBottomBar(
            selectedItem = BottomNavItem.DOCS,
            onItemSelected = {}
        )
    }
}