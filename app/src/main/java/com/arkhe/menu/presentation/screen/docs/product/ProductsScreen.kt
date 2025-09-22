package com.arkhe.menu.presentation.screen.docs.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.components.EmptyUI
import com.arkhe.menu.presentation.components.LoadingIndicatorSpinner
import com.arkhe.menu.presentation.screen.docs.product.content.BottomSheetProduct
import com.arkhe.menu.presentation.screen.docs.product.screen.BottomSheetProductGroup
import com.arkhe.menu.presentation.screen.docs.product.screen.HeaderAccordions
import com.arkhe.menu.presentation.screen.docs.product.screen.ProductListItem
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Info
import compose.icons.evaicons.outline.Refresh
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
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
    var showGroupBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        when (productsState) {
            is SafeApiResult.Loading -> {
                LoadingIndicatorSpinner(
                    message = stringResource(R.string.products)
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeaderAccordions(
                        title = stringResource(R.string.products)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    selectedGroup?.let { group ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { showGroupBottomSheet = true }
                                ) {
                                    Icon(
                                        imageVector = EvaIcons.Outline.Info,
                                        contentDescription = null
                                    )
                                }
                                Button(
                                    onClick = { showGroupBottomSheet = true }
                                ) {
                                    Text(group.seriesName)
                                }
                                IconButton(
                                    onClick = {
                                        productViewModel.clearSelectedGroup()
                                    }
                                ) {
                                    Icon(
                                        imageVector = EvaIcons.Outline.Refresh,
                                        contentDescription = null
                                    )
                                }
                            }
                            Text(
                                text = group.products.size.toString() + " products",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } ?: run {
                        Button(
                            onClick = { showGroupBottomSheet = true }
                        ) {
                            Text("Browse Groups")
                        }
                    }
                }

                selectedGroup?.let { group ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(0.dp)
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
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyUI(
                            message = "Select Product Groups",
                            isButtonLoad = false
                        )
                    }
                }
            }
        }
    }

    if (showGroupBottomSheet) {
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        ModalBottomSheet(
            onDismissRequest = {
                showGroupBottomSheet = false
            },
            sheetState = sheetState
        ) {
            BottomSheetProductGroup(
                productGroups = productGroups,
                onProductGroupClick = { group ->
                    productViewModel.selectProductGroup(group)
                    showGroupBottomSheet = false
                }
            )
        }
    }

    /*Product detail bottom sheet; TODO::Change to new screen*/
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

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule
            )
        }
    ) {
        AppTheme {
            ProductsScreen()
        }
    }
}