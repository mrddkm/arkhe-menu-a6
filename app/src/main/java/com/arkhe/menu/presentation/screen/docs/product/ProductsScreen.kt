package com.arkhe.menu.presentation.screen.docs.product

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.screen.docs.product.content.BottomSheetProduct
import com.arkhe.menu.presentation.screen.docs.product.screen.ProductGroup
import com.arkhe.menu.presentation.screen.docs.product.screen.ProductListItem
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
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

    var isRefreshing by remember { mutableStateOf(false) }
    val lastSuccess = remember { mutableStateOf<List<Product>?>(null) }

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDetailBottomSheet by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(ENGLISH) }
    var showGroupSelection by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        productViewModel.ensureDataLoaded()
    }

    LaunchedEffect(productsState) {
        if (productsState !is SafeApiResult.Loading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(productsState) {
        if (productsState is SafeApiResult.Success) {
            lastSuccess.value = (productsState as SafeApiResult.Success<List<Product>>).data
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Products",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                // Language toggle (only show if both languages have content)
                val actionInfo = productViewModel.getActionInfo(currentLanguage)
                if (actionInfo.isNotEmpty() &&
                    productViewModel.getActionInfo("id").isNotEmpty() &&
                    productViewModel.getActionInfo("en").isNotEmpty()
                ) {
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
                is SafeApiResult.Success -> {
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
                is SafeApiResult.Loading -> {
                    LoadingIndicator(
                        message = "Loading products...",
                    )
                }

                is SafeApiResult.Error -> {
                    if (!lastSuccess.value.isNullOrEmpty()) {
                        Text(
                            text = "Failed sync, displaying old data",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text(
                                    text = "Error loading products",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )

                                (productsState as SafeApiResult.Error).exception.message?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        isRefreshing = true
                                        productViewModel.refreshProducts()
                                    },
                                    enabled = !isRefreshing
                                ) {
                                    Text(if (isRefreshing) "Retrying..." else "Try Again")
                                }
                            }
                        }
                    }
                }

                is SafeApiResult.Success -> {
                    val products = (productsState as SafeApiResult.Success<List<Product>>).data
                    Log.d("ProductsScreen", "products: $products")
                    Log.d("ProductsScreen", "productGroups: $productGroups")

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
                                ProductGroup(
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
            BottomSheetProduct(
                product = selectedProduct!!
            )
        }
    }
}