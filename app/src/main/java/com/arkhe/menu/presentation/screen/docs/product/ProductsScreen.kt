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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.screen.docs.product.content.BottomSheetProduct
import com.arkhe.menu.presentation.screen.docs.product.screen.BottomSheetProductGroup
import com.arkhe.menu.presentation.screen.docs.product.screen.ProductListItem
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Globe
import compose.icons.evaicons.outline.Info
import compose.icons.evaicons.outline.MessageSquare
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
    var currentLanguage by remember { mutableStateOf(ENGLISH) }

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
                .padding(8.dp)
        ) {
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
                    /*TODO:: Separated (reusable compose) for actionInfo header*/
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val actionInfo = productViewModel.getActionInfo(currentLanguage)
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

                            if (actionInfo.isNotEmpty() &&
                                productViewModel.getActionInfo("id").isNotEmpty() &&
                                productViewModel.getActionInfo("en").isNotEmpty()
                            ) {
                                IconButton(
                                    onClick = {
                                        currentLanguage =
                                            if (currentLanguage == "en") "id" else "en"
                                    }
                                ) {
                                    Icon(
                                        imageVector = EvaIcons.Outline.Globe,
                                        contentDescription = "Toggle Language"
                                    )
                                }
                            }
                        }
                        if (actionInfo.isNotEmpty()) {
                            Text(
                                text = actionInfo,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        selectedGroup?.let { group ->
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
                        } ?: run {
                            Button(
                                onClick = { showGroupBottomSheet = true }
                            ) {
                                Text("Browse Products")
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
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = EvaIcons.Outline.MessageSquare,
                                    contentDescription = null
                                )
                                Text(
                                    text = "Select Products Group",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /*Section 2: Bottom sheet for Product Groups*/
    if (showGroupBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showGroupBottomSheet = false
            }
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