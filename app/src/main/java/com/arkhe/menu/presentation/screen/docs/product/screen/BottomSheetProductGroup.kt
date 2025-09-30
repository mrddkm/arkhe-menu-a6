package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.ProductByGroup
import com.arkhe.menu.presentation.ui.components.HeaderLabel

@Composable
fun BottomSheetProductGroup(
    productByGroups: List<ProductByGroup>,
    onProductGroupClick: (ProductByGroup) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderLabel(
            label = stringResource(R.string.product_group)
        )
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(productByGroups) { group ->
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
}