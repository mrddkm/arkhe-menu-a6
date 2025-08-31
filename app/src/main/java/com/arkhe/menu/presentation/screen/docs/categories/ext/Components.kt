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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun CategoriesSection(
    categoriesList: List<Category>,
    onCategoriesClick: (Category) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categoriesList) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoriesClick(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val backgroundColor = parseColorFromHex(category.colors.backgroundColor)
    val iconColor = parseColorFromHex(category.colors.iconColor)

    Spacer(modifier = Modifier.width(8.dp))
    Card(
        modifier = Modifier
            .width(170.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Egg,
                    contentDescription = category.name,
                    modifier = Modifier.size(24.dp),
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.productCount} Products",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
            }
        }
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
fun CategoryCardPreview() {
    AppTheme {
        CategoriesSection(
            categoriesList = listOf(
                Category(
                    id = "1",
                    name = "Fruits",
                    type = "food",
                    productCount = 120,
                    initiation = 0,
                    research = 0,
                    ready = 0,
                    information = com.arkhe.menu.domain.model.CategoryInformation(
                        indonesian = "Kategori Buah",
                        english = "Fruit Category"
                    ),
                    colors = com.arkhe.menu.domain.model.CategoryColors(
                        backgroundColor = "#FFEB3B",
                        iconColor = "#F57C00"
                    ),
                    actionInfo = com.arkhe.menu.domain.model.CategoryActionInfo(
                        action = "view",
                        information = com.arkhe.menu.domain.model.CategoryInformation(
                            indonesian = "Lihat Kategori Buah",
                            english = "View Fruit Category"
                        )
                    )
                ),
                Category(
                    id = "2",
                    name = "Vegetables",
                    type = "food",
                    productCount = 80,
                    initiation = 0,
                    research = 0,
                    ready = 0,
                    information = com.arkhe.menu.domain.model.CategoryInformation(
                        indonesian = "Kategori Sayur",
                        english = "Vegetable Category"
                    ),
                    colors = com.arkhe.menu.domain.model.CategoryColors(
                        backgroundColor = "#4CAF50",
                        iconColor = "#1B5E20"
                    ),
                    actionInfo = com.arkhe.menu.domain.model.CategoryActionInfo(
                        action = "view",
                        information = com.arkhe.menu.domain.model.CategoryInformation(
                            indonesian = "Lihat Kategori Sayur",
                            english = "View Vegetable Category"
                        )
                    )
                )
            ),
            onCategoriesClick = {}
        )
    }
}
