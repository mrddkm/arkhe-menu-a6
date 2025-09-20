package com.arkhe.menu.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.arkhe.menu.presentation.ui.components.BottomNavItem
import com.arkhe.menu.presentation.ui.components.GlassScaffold
import com.arkhe.menu.presentation.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun GlassScaffoldPreviewLight() {
    AppTheme(darkTheme = false) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        val items = listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Search", Icons.Default.Search),
            BottomNavItem("Profile", Icons.Default.Person)
        )

        GlassScaffold(
            title = "Docs",
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GlassScaffoldPreviewDark() {
    AppTheme(darkTheme = true) {
        var selectedIndex by remember { mutableIntStateOf(1) }
        val items = listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Search", Icons.Default.Search),
            BottomNavItem("Profile", Icons.Default.Person)
        )

        GlassScaffold(
            title = "Docs",
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it }
        )
    }
}
