package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.domain.model.ProductInformationLanguage
import com.arkhe.menu.presentation.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ChevronRight

@Composable
fun ProductGroup(
    group: ProductGroup,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 0.dp, end = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .padding(start = 0.dp, top = 0.dp, bottom = 6.dp, end = 0.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.mn),
                contentDescription = null,
                modifier = Modifier.size(42.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 10.dp, bottom = 0.dp, end = 0.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = group.seriesName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 0.dp),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
        }
    }

}

@Composable
fun ProductListItem(
    product: Product,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 12.dp, bottom = 0.dp, end = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(0.dp)
            ) {
                Text(
                    text = product.productDestination,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.productFullName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Row(
                modifier = Modifier.padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusDevelopmentChip(product.status)
                Icon(
                    imageVector = EvaIcons.Outline.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 0.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductGroupPreview() {
    AppTheme {
        val sampleGroup = ProductGroup(
            seriesName = "Mountain Series",
            products = listOf(
                sampleProduct
            )
        )
        ProductGroup(
            group = sampleGroup,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview() {
    AppTheme {
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
        ProductListItem(
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