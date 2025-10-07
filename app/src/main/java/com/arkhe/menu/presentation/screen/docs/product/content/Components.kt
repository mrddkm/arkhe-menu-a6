package com.arkhe.menu.presentation.screen.docs.product.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.sampleProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductUI(
    navController: NavController,
    productList: List<Product>
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDetailBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(productList.chunked(2)) { productChunk ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .padding(start = 16.dp, bottom = 4.dp)
                ) {
                    productChunk.forEach { product ->
                        ProductCardContent(
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
    if (showDetailBottomSheet && selectedProduct != null) {
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        ModalBottomSheet(
            onDismissRequest = {
                showDetailBottomSheet = false
                selectedProduct = null
            },
            sheetState = sheetState
        ) {
            BottomSheetProduct(
                product = selectedProduct!!,
                onMoreClick = {
                    navController.navigate(
                        NavigationRoute.productDetail(
                            productId = selectedProduct!!.id,
                            source = NavigationRoute.DOCS
                        )
                    )
                    showDetailBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun ProductCardContent(
    product: Product,
    onClick: () -> Unit
) {
    Surface(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = product.productDestination,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = product.productFullName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = sourceCodeProFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductUIPreview() {
    ArkheTheme {
        ProductUI(
            navController = NavController(LocalContext.current),
            productList = listOf(
                sampleProduct,
                sampleProduct,
                sampleProduct,
                sampleProduct,
                sampleProduct,
                sampleProduct
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductSectionPreview() {
    ArkheTheme {
        ProductCardContent(
            product = sampleProduct,
            onClick = {}
        )
    }
}