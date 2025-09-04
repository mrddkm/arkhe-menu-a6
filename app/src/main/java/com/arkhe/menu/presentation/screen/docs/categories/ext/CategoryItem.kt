@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.categories.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryActionInfo
import com.arkhe.menu.domain.model.CategoryColors
import com.arkhe.menu.domain.model.CategoryInformation
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDetailView: Boolean = false
) {
    val backgroundColor = parseColorFromHex(category.colors.backgroundColor)
    val iconColor = parseColorFromHex(category.colors.iconColor)

    if (isDetailView) {
        // Detail view layout - expanded information
        Card(
            modifier = modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with icon and basic info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Egg,
                            contentDescription = category.name,
                            modifier = Modifier.size(40.dp),
                            tint = iconColor
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = category.type,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Product Statistics
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Product Statistics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatisticItemDetail(
                                label = "Total Products",
                                value = category.productCount.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            StatisticItemDetail(
                                label = "Ready",
                                value = category.ready.toString(),
                                color = Color(0xFF4CAF50)
                            )
                            StatisticItemDetail(
                                label = "Research",
                                value = category.research.toString(),
                                color = Color(0xFFFF9800)
                            )
                            StatisticItemDetail(
                                label = "Initiation",
                                value = category.initiation.toString(),
                                color = Color(0xFF2196F3)
                            )
                        }
                    }
                }

                // Information Section
                if (category.information.indonesian.isNotBlank() || category.information.english.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Information",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (category.information.indonesian.isNotBlank()) {
                                Text(
                                    text = category.information.indonesian,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (category.information.english.isNotBlank() &&
                                category.information.english != category.information.indonesian
                            ) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = category.information.english,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // Action Information Section
                if (category.actionInfo.action.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Action: ${category.actionInfo.action}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (category.actionInfo.information.indonesian.isNotBlank()) {
                                Text(
                                    text = category.actionInfo.information.indonesian,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (category.actionInfo.information.english.isNotBlank() &&
                                category.actionInfo.information.english != category.actionInfo.information.indonesian
                            ) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = category.actionInfo.information.english,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // List view layout - compact information
        Card(
            modifier = modifier
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

                    if (category.research > 0 || category.initiation > 0 || category.ready > 0) {
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
}

@Composable
fun StatisticItemDetail(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
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

fun parseColorFromHex(hexColor: String): Color {
    return try {
        val cleanHex = hexColor.removePrefix("0x").removePrefix("#")
        Color(cleanHex.toLong(16) or 0x00000000FF000000)
    } catch (_: Exception) {
        Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val sampleCategory = Category(
        id = "1",
        name = "Fruits & Vegetables",
        type = "Food Category",
        productCount = 120,
        initiation = 10,
        research = 20,
        ready = 90,
        information = CategoryInformation(
            indonesian = "Kategori buah-buahan dan sayuran segar untuk kebutuhan sehari-hari",
            english = "Fresh fruits and vegetables category for daily needs"
        ),
        colors = CategoryColors(
            backgroundColor = "#FFEB3B",
            iconColor = "#F57C00"
        ),
        actionInfo = CategoryActionInfo(
            action = "View Products",
            information = CategoryInformation(
                indonesian = "Lihat produk dalam kategori ini",
                english = "View products in this category"
            )
        )
    )

    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "List View:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            CategoryItem(
                category = sampleCategory,
                onClick = {},
                isDetailView = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Detail View:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            CategoryItem(
                category = sampleCategory,
                onClick = {},
                isDetailView = true
            )
        }
    }
}