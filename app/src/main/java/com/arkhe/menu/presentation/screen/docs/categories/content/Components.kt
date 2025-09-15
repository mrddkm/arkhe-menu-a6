package com.arkhe.menu.presentation.screen.docs.categories.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryActionInfo
import com.arkhe.menu.domain.model.CategoryColors
import com.arkhe.menu.domain.model.CategoryInformationLanguage
import com.arkhe.menu.presentation.screen.docs.categories.screen.parseColorFromHex
import com.arkhe.menu.presentation.theme.AppTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Droplet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesNonScrollableUI(
    categoriesList: List<Category>
) {
    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoriesList.forEach { category ->
            CategoryItemNonScrollable(
                category = category,
                onClick = {
                    selectedCategory = category
                    showCategoryBottomSheet = true
                }
            )
        }
    }

    if (showCategoryBottomSheet && selectedCategory != null) {
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        ModalBottomSheet(
            onDismissRequest = {
                showCategoryBottomSheet = false
                selectedCategory = null
            },
            sheetState = sheetState
        ) {
            BottomSheetCategory(
                category = selectedCategory!!
            )
        }
    }
}

@Composable
fun CategoryItemNonScrollable(
    category: Category,
    onClick: () -> Unit
) {
    val backgroundColor = parseColorFromHex(category.colors.backgroundColor)
    val iconColor = parseColorFromHex(category.colors.iconColor)

    Surface(onClick = { onClick() }, shape = MaterialTheme.shapes.small) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.Droplet,
                    contentDescription = category.name,
                    modifier = Modifier.size(24.dp),
                    tint = iconColor
                )
            }
            Column {
                Text(text = category.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${category.productCount} Products",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    AppTheme {
        @Suppress("SpellCheckingInspection")
        CategoriesNonScrollableUI(
            categoriesList = listOf(
                Category(
                    id = "SRS",
                    name = "Series",
                    type = "Regular",
                    productCount = 26,
                    initiation = 0,
                    research = 0,
                    ready = 0,
                    information = CategoryInformationLanguage(
                        indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
                        english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
                    ),
                    colors = CategoryColors(
                        backgroundColor = "0xFFE0F2F1",
                        iconColor = "0xFF00695C"
                    ),
                    actionInfo = CategoryActionInfo(
                        action = "productcategory",
                        information = CategoryInformationLanguage(
                            indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
                            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
                        )
                    )
                ),
                Category(
                    id = "CIP",
                    name = "Chipkeun",
                    type = "Udunan",
                    productCount = 11,
                    initiation = 0,
                    research = 0,
                    ready = 0,
                    information = CategoryInformationLanguage(
                        indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
                        english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
                    ),
                    colors = CategoryColors(
                        backgroundColor = "0xFFE0F2F1",
                        iconColor = "0xFF00695C"
                    ),
                    actionInfo = CategoryActionInfo(
                        action = "productcategory",
                        information = CategoryInformationLanguage(
                            indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
                            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
                        )
                    )
                )
            )
        )
    }
}