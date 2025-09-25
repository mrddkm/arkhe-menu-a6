@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.product.content

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.components.MoreSection
import com.arkhe.menu.presentation.ui.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants.Product.PRODUCT_TITLE
import com.arkhe.menu.utils.sampleProduct
import java.io.File

@Composable
fun BottomSheetProduct(
    product: Product,
    imagePath: String? = null,
    onMoreClick: () -> Unit = {}
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
            HeaderTitleSecondary(
                title = PRODUCT_TITLE
            )
            if (product.information.indonesian.isNotEmpty() && product.information.english.isNotEmpty()
            ) {
                IconButton(
                    onClick = { showEnglish = !showEnglish }
                ) {
                    if (showEnglish) {
                        LanguageIconEn()
                    } else {
                        LanguageIconId()
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
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = product.productFullName,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = sourceCodeProFontFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.categoryName}/${product.categoryType}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = informationText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                textAlign = TextAlign.Left
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        MoreSection(onMoreClick = { onMoreClick() })
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetProductPreview() {
    AppTheme {
        BottomSheetProduct(
            product = sampleProduct,
            imagePath = null,
            onMoreClick = {}
        )
    }
}