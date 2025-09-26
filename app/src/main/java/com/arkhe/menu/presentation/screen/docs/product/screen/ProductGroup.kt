package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.presentation.ui.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.sampleProduct
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import java.io.File

@Composable
fun ProductGroup(
    group: ProductGroup,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val representativeProduct = group.products.firstOrNull()
    val imagePath = representativeProduct?.localImagePath

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!imagePath.isNullOrBlank() && File(imagePath).exists()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(File(imagePath))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Product Group Image - ${group.seriesName}",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.image_outline),
                    fallback = painterResource(R.drawable.alert_triangle_outline),
                    error = painterResource(R.drawable.alert_triangle_outline)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.image_outline),
                    contentDescription = "Default Product Group Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
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
                    .padding(0.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = product.productDestination,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
            Row(
                modifier = Modifier.padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusDevelopmentChip(product.status)
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosForward,
                    contentDescription = null,
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
        ProductListItem(
            product = sampleProduct,
            onClick = {}
        )
    }
}