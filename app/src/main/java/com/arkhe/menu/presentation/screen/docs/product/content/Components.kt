package com.arkhe.menu.presentation.screen.docs.product.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductInformationLanguage
import com.arkhe.menu.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductUI(
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
                        .padding(start = 16.dp)
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
                imagePath = selectedProduct?.localImagePath
            )
        }
    }
}

@Composable
fun ProductCardContent(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = product.productDestination,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = product.productFullName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductUIPreview() {
    AppTheme {
        ProductUI(
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
    AppTheme {
        ProductCardContent(
            product = sampleProduct,
            onClick = {}
        )
    }
}

val sampleProduct = Product(
    id = "1",
    productCategoryId = "1",
    categoryName = "Sample Category",
    categoryType = "Sample Type",
    productCode = "Sample Code",
    productFullName = "Chipkeun #01",
    productDestination = "Gn. Pangradinan",
    logo = "",
    status = "Ready",
    information = ProductInformationLanguage(
        indonesian = "Sample Indonesian Information",
        english = "Sample English Information"
    ),
    actionInfo = ProductActionInfo(
        action = "Sample Action",
        information = ProductInformationLanguage(
            indonesian = "Sample Indonesian Action Information",
            english = "Sample English Action Information"
        )
    )
)