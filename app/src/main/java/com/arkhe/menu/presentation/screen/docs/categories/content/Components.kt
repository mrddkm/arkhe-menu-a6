package com.arkhe.menu.presentation.screen.docs.categories.content

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryActionInfo
import com.arkhe.menu.domain.model.CategoryColors
import com.arkhe.menu.domain.model.CategoryInformation
import com.arkhe.menu.presentation.screen.docs.categories.screen.parseColorFromHex
import com.arkhe.menu.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesUI(
    categoriesList: List<Category>
) {
    /*Category*/
    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categoriesList) { category ->
                CategoryCardContent(
                    category = category,
                    onClick = {
                        selectedCategory = category
                        showCategoryBottomSheet = true
                    }
                )
            }
        }
    }

    /*Category Detail BottomSheet*/
    if (showCategoryBottomSheet && selectedCategory != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showCategoryBottomSheet = false
                selectedCategory = null
            }
        ) {
            BottomSheetCategory(
                category = selectedCategory!!
            )
        }
    }
}

@Composable
fun CategoryCardContent(
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

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    AppTheme {
        CategoriesUI(
            categoriesList = listOf(
                Category(
                    id = "1",
                    name = "Fruits",
                    type = "food",
                    productCount = 120,
                    initiation = 0,
                    research = 0,
                    ready = 0,
                    information = CategoryInformation(
                        indonesian = "Kategori Buah",
                        english = "Fruit Category"
                    ),
                    colors = CategoryColors(
                        backgroundColor = "#FFEB3B",
                        iconColor = "#F57C00"
                    ),
                    actionInfo = CategoryActionInfo(
                        action = "view",
                        information = CategoryInformation(
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
                    information = CategoryInformation(
                        indonesian = "Kategori Sayur",
                        english = "Vegetable Category"
                    ),
                    colors = CategoryColors(
                        backgroundColor = "#4CAF50",
                        iconColor = "#1B5E20"
                    ),
                    actionInfo = CategoryActionInfo(
                        action = "view",
                        information = CategoryInformation(
                            indonesian = "Lihat Kategori Sayur",
                            english = "View Vegetable Category"
                        )
                    )
                )
            )
        )
    }
}