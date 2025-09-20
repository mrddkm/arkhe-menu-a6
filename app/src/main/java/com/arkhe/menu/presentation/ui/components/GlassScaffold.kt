package com.arkhe.menu.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import kotlin.math.min

data class BottomNavItem(val label: String, val icon: ImageVector)

@SuppressLint("RestrictedApi")
@Composable
fun GlassScaffold(
    title: String,
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollOffset by remember {
        derivedStateOf { min(1f, listState.firstVisibleItemScrollOffset / 200f) }
    }

    Scaffold(
        topBar = {
            GlassTopBar(title = title, listState = listState)
        },
        bottomBar = {
            GlassBottomBar(
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = listState
        ) {
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 34.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                        .graphicsLayer {
                            val scale = lerp(1f, 0.7f, scrollOffset)
                            scaleX = scale
                            scaleY = scale
                            translationY = -40 * scrollOffset
                            alpha = 1f - scrollOffset
                        }
                )
            }
            items(30) { i ->
                Text(
                    text = "Item $i",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
