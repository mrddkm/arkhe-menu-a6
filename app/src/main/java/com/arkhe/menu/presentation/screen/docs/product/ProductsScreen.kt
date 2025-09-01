package com.arkhe.menu.presentation.screen.docs.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.presentation.screen.docs.product.ext.ProductDetailBottomSheet
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = koinViewModel()
) {
    val productsState by productViewModel.productsState.collectAsState()
    val productGroups by productViewModel.productGroups.collectAsState()
    val selectedGroup by productViewModel.selectedGroup.collectAsState()
    val filteredProducts by productViewModel.filteredProducts.collectAsState()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDetailBottomSheet by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf("en") }
    var showGroupSelection by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with language toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Products Tripkeun",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            // Language toggle (only show if both languages have content)
            val actionInfo = productViewModel.getActionInfo(currentLanguage)
            if (actionInfo.isNotEmpty() &&
                productViewModel.getActionInfo("id").isNotEmpty() &&
                productViewModel.getActionInfo("en").isNotEmpty()) {
                IconButton(
                    onClick = {
                        currentLanguage = if (currentLanguage == "en") "id" else "en"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Toggle Language"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action information
        when (productsState) {
            is ApiResult.Success -> {
                val actionInfo = productViewModel.getActionInfo(currentLanguage)
                if (actionInfo.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = actionInfo,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            else -> {}
        }

        when (productsState) {
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
                if (showGroupSelection) {
                    // Show product groups (categories)
                    Text(
                        text = "Product Categories",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productGroups) { group ->
                            ProductGroupCard(
                                group = group,
                                onClick = {
                                    productViewModel.selectProductGroup(group)
                                    showGroupSelection = false
                                }
                            )
                        }
                    }
                } else {
                    // Show products in selected group
                    selectedGroup?.let { group ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = group.seriesName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Button(
                                onClick = {
                                    showGroupSelection = true
                                    productViewModel.clearSelectedGroup()
                                }
                            ) {
                                Text("Back to Categories")
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredProducts) { product ->
                                ProductListItem(
                                    product = product,
                                    onClick = {
                                        selectedProduct = product
                                        showDetailBottomSheet = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            is ApiResult.Error -> {
                Column {
                    Text(
                        text = "Error loading products",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = (productsState as ApiResult.Error).exception.message ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { productViewModel.refreshProducts() }
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }

    // Product detail bottom sheet
    if (showDetailBottomSheet && selectedProduct != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showDetailBottomSheet = false
                selectedProduct = null
            }
        ) {
            ProductDetailBottomSheet(
                product = selectedProduct!!,
                onDismiss = {
                    showDetailBottomSheet = false
                    selectedProduct = null
                }
            )
        }
    }
}

@Composable
fun ProductGroupCard(
    group: ProductGroup,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = group.seriesName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${group.products.size} Products",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Show status counts
            val statusCounts = group.products.groupingBy { it.status }.eachCount()
            if (statusCounts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statusCounts.forEach { (status, count) ->
                        StatusChip(
                            text = "$count $status",
                            color = when (status) {
                                "Ready" -> Color(0xFF4CAF50)
                                "Research" -> Color(0xFFFF9800)
                                "Product" -> Color(0xFF2196F3)
                                else -> Color(0xFF757575)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.productCode,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                StatusChip(
                    text = product.status,
                    color = when (product.status) {
                        "Ready" -> Color(0xFF4CAF50)
                        "Research" -> Color(0xFFFF9800)
                        "Product" -> Color(0xFF2196F3)
                        else -> Color(0xFF757575)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.productFullName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.productDestination,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
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