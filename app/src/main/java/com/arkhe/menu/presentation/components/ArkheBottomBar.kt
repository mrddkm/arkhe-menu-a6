@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GroupWork
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.BottomNavItem
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Activity
import compose.icons.evaicons.outline.Grid

@Composable
fun ArkheGlassBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    scrollAlpha: Float = 1f
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = scrollAlpha,
        animationSpec = tween(300),
        label = "bottom_bar_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(RoundedCornerShape(34.dp))
                .alpha(animatedAlpha),
            shape = RoundedCornerShape(34.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(8.dp) // Reduced elevation
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.75f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .alpha(animatedAlpha),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.entries.forEach { item ->
                IOSNavigationBarItem(
                    selected = selectedItem == item,
                    onClick = { onItemSelected(item) },
                    icon = getIconForItem(item),
                    label = item.title,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun IOSNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        animationSpec = tween(200),
        label = "icon_color"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        animationSpec = tween(200),
        label = "text_color"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(200),
        label = "icon_scale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp)),
        color = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = androidx.compose.ui.unit.TextUnit.Unspecified
                ),
                color = textColor,
                maxLines = 1
            )
        }
    }
}

private fun getIconForItem(item: BottomNavItem): ImageVector {
    return when (item) {
        BottomNavItem.DOCS -> EvaIcons.Outline.Grid
        BottomNavItem.TRIPKEUN -> Icons.Rounded.GroupWork
        BottomNavItem.ACTIVITY -> EvaIcons.Outline.Activity
    }
}

@Preview(showBackground = true)
@Composable
fun ArkheGlassBottomBarPreview() {
    var selectedItem = BottomNavItem.DOCS
    AppTheme {
        ArkheGlassBottomBar(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            scrollAlpha = 1f
        )
    }
}
