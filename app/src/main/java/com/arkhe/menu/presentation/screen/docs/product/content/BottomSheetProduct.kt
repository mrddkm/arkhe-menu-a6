@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.product.content

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.components.MoreSection
import com.arkhe.menu.presentation.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Globe
import java.io.File

@Composable
fun BottomSheetProduct(
    product: Product,
    imagePath: String? = null
) {
    var showEnglish by remember { mutableStateOf(false) }
    val finalImagePath = product.localImagePath ?: imagePath

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product.information.indonesian.isNotEmpty() &&
                product.information.english.isNotEmpty()
            ) {
                Spacer(Modifier.width(48.dp))
            }
            Text(
                text = Constants.Product.PRODUCT_LABEL,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            if (product.information.indonesian.isNotEmpty() && product.information.english.isNotEmpty()
            ) {
                IconButton(
                    onClick = { showEnglish = !showEnglish }
                ) {
                    if (showEnglish) {
                        Icon(
                            imageVector = EvaIcons.Outline.Globe,
                            contentDescription = "Toggle Language English",
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_id_indonesia),
                            contentDescription = "Toggle Language Indonesia",
                            modifier = Modifier
                                .size(24.dp)
                                .border(0.5.dp, Color.LightGray, shape = CircleShape),
                        )
                    }
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    !finalImagePath.isNullOrEmpty() -> {
                        val imageFile = File(finalImagePath)
                        if (imageFile.exists() && imageFile.length() > 0) {
                            AsyncImage(
                                model = Uri.fromFile(imageFile),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.image_outline),
                                error = painterResource(R.drawable.alert_triangle_outline),
                                onLoading = {
                                    Log.d("BottomSheetProduct", "🔄 Image loading: $finalImagePath")
                                },
                                onSuccess = {
                                    Log.d(
                                        "BottomSheetProduct",
                                        "✅ Image loaded successfully: $finalImagePath"
                                    )
                                },
                                onError = { error ->
                                    Log.e(
                                        "BottomSheetProduct",
                                        "❌ Error: ${error.result.throwable.message}"
                                    )
                                }
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.image_outline),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    product.logo.isNotEmpty() -> {
                        AsyncImage(
                            model = product.logo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.image_outline),
                            error = painterResource(R.drawable.alert_triangle_outline),
                            onError = { error ->
                                Log.e(
                                    "BottomSheetProduct",
                                    "❌ Error: ${error.result.throwable.message}"
                                )
                            },
                            onSuccess = {
                                Log.d(
                                    "BottomSheetProduct",
                                    "✅ URL Image loaded successfully: ${product.logo}"
                                )
                            }
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.image_outline),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "“${product.productDestination}”",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = product.productFullName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productCode,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.categoryName}/${product.categoryType}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    StatusDevelopmentChip(product.status)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        val informationText = remember(showEnglish, product.information) {
            when {
                showEnglish && product.information.english.isNotBlank() ->
                    product.information.english

                product.information.indonesian.isNotBlank() ->
                    product.information.indonesian

                product.information.english.isNotBlank() ->
                    product.information.english

                else -> "No information available"
            }
        }
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
                    text = informationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        MoreSection(onMoreClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetProductPreview() {
    val sampleProduct = Product(
        id = "rtg6wm5iijqC5WIl",
        productCategoryId = "SRS",
        categoryName = "Series",
        categoryType = "Regular",
        productCode = "MN04",
        productFullName = "Mountain Series #04",
        productDestination = "Gn. Papandayan",
        logo = "",
        status = STATISTICS_RESEARCH,
        information = com.arkhe.menu.domain.model.ProductInformationLanguage(
            indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
        ),
        actionInfo = com.arkhe.menu.domain.model.ProductActionInfo(
            action = "product",
            information = com.arkhe.menu.domain.model.ProductInformationLanguage(
                indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
                english = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."
            )
        ),
        localImagePath = null
    )
    AppTheme {
        BottomSheetProduct(
            product = sampleProduct,
            imagePath = null
        )
    }
}