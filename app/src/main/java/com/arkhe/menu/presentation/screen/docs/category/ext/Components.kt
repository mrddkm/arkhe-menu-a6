@file:Suppress("SpellCheckingInspection")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.arkhe.menu.presentation.screen.docs.category.ext

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun CategoriesSection(
    categoriesList: List<Categories>,
    onCategoriesClick: (Categories) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categoriesList) { categories ->
                CategoriesCard(
                    categories = categories,
                    onClick = { onCategoriesClick(categories) }
                )
            }
        }
    }
}

@Composable
fun CategoriesCard(
    categories: Categories,
    onClick: () -> Unit
) {
    val categoryColor = getCategoryColor(categories.nama)
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
                    .background(categoryColor.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Egg,
                    contentDescription = categories.nama,
                    modifier = Modifier.size(24.dp),
                    tint = categoryColor.iconColor
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
                    text = categories.nama,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${categories.productCount} Products",
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
        CategoriesSection(
            categoriesList = sampleCategories,
            onCategoriesClick = {}
        )
    }
}
