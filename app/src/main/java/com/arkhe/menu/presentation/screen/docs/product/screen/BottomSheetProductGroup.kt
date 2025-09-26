package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.presentation.ui.components.HeaderBottomSheet
import com.arkhe.menu.presentation.ui.components.HeaderLabel
import com.arkhe.menu.utils.Constants.Product.PRODUCT_GROUP_LABEL

@Composable
fun BottomSheetProductGroup(
    productGroups: List<ProductGroup>,
    onProductGroupClick: (ProductGroup) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderLabel(
            label = PRODUCT_GROUP_LABEL
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(productGroups) { group ->
                ProductGroup(
                    group = group,
                    onClick = {
                        onProductGroupClick(group)
                    }
                )
            }
        }
    }
}