@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.product

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductInformationLanguage
import com.arkhe.menu.presentation.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Globe
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    source: String,
    onBackClick: () -> Unit,
    productViewModel: ProductViewModel = koinViewModel(),
    imagePath: String? = null
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var showEnglish by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        product = productViewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product Detail",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = EvaIcons.Outline.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        product?.let { productData ->
            ProductDetailContent(
                product = productData,
                imagePath = imagePath,
                showEnglish = showEnglish,
                onLanguageToggle = { showEnglish = !showEnglish },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Product not found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    imagePath: String?,
    showEnglish: Boolean,
    onLanguageToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val finalImagePath = product.localImagePath ?: imagePath

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with language toggle
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
            if (product.information.indonesian.isNotEmpty() &&
                product.information.english.isNotEmpty()
            ) {
                IconButton(onClick = onLanguageToggle) {
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

        Spacer(modifier = Modifier.height(24.dp))

        // Product header with image and basic info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(80.dp)
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
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.image_outline),
                                error = painterResource(R.drawable.alert_triangle_outline),
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.image_outline),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }

                    product.logo.isNotEmpty() -> {
                        AsyncImage(
                            model = product.logo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.image_outline),
                            error = painterResource(R.drawable.alert_triangle_outline),
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.image_outline),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            // Product Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.productDestination,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.productFullName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = sourceCodeProFontFamily,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productCode,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${product.categoryName}/${product.categoryType}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusDevelopmentChip(product.status)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Product Information
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
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = informationText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Information if available
        product.actionInfo?.information?.let { actionInfo ->
            val actionText = remember(showEnglish, actionInfo) {
                when {
                    showEnglish && actionInfo.english.isNotBlank() ->
                        actionInfo.english

                    actionInfo.indonesian.isNotBlank() ->
                        actionInfo.indonesian

                    actionInfo.english.isNotBlank() ->
                        actionInfo.english

                    else -> null
                }
            }

            actionText?.let { text ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Additional Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
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
        information = ProductInformationLanguage(
            indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun.",
            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante."
        ),
        actionInfo = ProductActionInfo(
            action = "product",
            information = ProductInformationLanguage(
                indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an, ketika pencetak yang tidak dikenal mengambil galley jenis dan merombaknya untuk membuat buku spesimen jenis.",
                english = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        ),
        localImagePath = null
    )
    AppTheme {
        ProductDetailContent(
            product = sampleProduct,
            imagePath = null,
            showEnglish = false,
            onLanguageToggle = {}
        )
    }
}