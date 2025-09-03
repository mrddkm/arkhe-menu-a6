package com.arkhe.menu.presentation.screen.docs.product.ext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.utils.Constants

@Composable
fun BottomSheetProduct(
    product: Product
) {
    var currentLanguage by remember { mutableStateOf(Constants.CurrentLanguage.ENGLISH) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Constants.Product.PRODUCT_LABEL,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "“${product.productDestination}”",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (product.information.indonesian.isNotEmpty() &&
                product.information.english.isNotEmpty()
            ) {
                IconButton(
                    onClick = {
                        currentLanguage =
                            if (currentLanguage == Constants.CurrentLanguage.ENGLISH)
                                Constants.CurrentLanguage.INDONESIAN
                            else Constants.CurrentLanguage.ENGLISH
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Language,
                        contentDescription = "Toggle Language"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.productFullName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.productCode,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = product.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = "/",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = product.categoryType,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (product.status) {
                        "Ready" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        "Research" -> Color(0xFFFF9800).copy(alpha = 0.1f)
                        "Product" -> Color(0xFF2196F3).copy(alpha = 0.1f)
                        else -> Color(0xFF757575).copy(alpha = 0.1f)
                    }
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = product.status,
                    style = MaterialTheme.typography.labelLarge,
                    color = when (product.status) {
                        "Ready" -> Color(0xFF4CAF50)
                        "Research" -> Color(0xFFFF9800)
                        "Product" -> Color(0xFF2196F3)
                        else -> Color(0xFF757575)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        val information = if (currentLanguage == Constants.CurrentLanguage.INDONESIAN) {
            product.information.indonesian
        } else {
            product.information.english
        }
        if (information.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = information,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetProductPreview() {
    val sampleProduct = Product(
        id = "rtg6wm5iijqC5WIl",
        productCategoryId = "SRS",
        categoryName = "Series",
        categoryType = "Reguler",
        productCode = "MN04",
        productFullName = "Mountain Series #04",
        productDestination = "Gn. Papandayan",
        status = "Ready",
        information = com.arkhe.menu.domain.model.ProductInformation(
            indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
        ),
        actionInfo = com.arkhe.menu.domain.model.ProductActionInfo(
            action = "product",
            information = com.arkhe.menu.domain.model.ProductInformation(
                indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
                english = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."
            )
        )
    )
    AppTheme {
        BottomSheetProduct(product = sampleProduct)
    }
}