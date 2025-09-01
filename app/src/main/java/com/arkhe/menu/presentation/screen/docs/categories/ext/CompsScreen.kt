package com.arkhe.menu.presentation.screen.docs.categories.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Category

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val backgroundColor = parseColorFromHex(category.colors.backgroundColor)
    val iconColor = parseColorFromHex(category.colors.iconColor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Egg,
                    contentDescription = category.name,
                    modifier = Modifier.size(28.dp),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${category.productCount} Products • ${category.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                if (category.research > 0 || category.initiation > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        if (category.ready > 0) {
                            StatusChip(
                                text = "${category.ready} Ready",
                                color = Color(0xFF4CAF50)
                            )
                        }
                        if (category.research > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusChip(
                                text = "${category.research} Research",
                                color = Color(0xFFFF9800)
                            )
                        }
                        if (category.initiation > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusChip(
                                text = "${category.initiation} Initiation",
                                color = Color(0xFF2196F3)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}