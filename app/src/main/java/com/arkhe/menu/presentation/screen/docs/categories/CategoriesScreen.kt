package com.arkhe.menu.presentation.screen.docs.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.presentation.screen.docs.categories.ext.CategoryItem
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = koinViewModel()
) {
    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Category Tripkeun",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (categoriesState) {
            is ApiResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResult.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((categoriesState as ApiResult.Success<List<Category>>).data) { category ->
                        CategoryItem(
                            category = category,
                            onClick = {}
                        )
                    }
                }
            }

            is ApiResult.Error -> {
                Column {
                    Text(
                        text = "Error loading categories",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = (categoriesState as ApiResult.Error).exception.message
                            ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { categoryViewModel.refreshCategories() }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}