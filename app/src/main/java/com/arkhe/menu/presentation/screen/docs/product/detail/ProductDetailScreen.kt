package com.arkhe.menu.presentation.screen.docs.product.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductInformationLanguage
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    source: String,
    onBackClick: () -> Unit,
    navController: NavController? = null,
    productViewModel: ProductViewModel = koinViewModel(),
    imagePath: String? = null
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var showEnglish by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        product = productViewModel.getProductById(productId)
    }

    val handleBackNavigation: () -> Unit = {
        navController?.let { nav ->
            val popSuccess = when (source) {
                NavigationRoute.PRODUCTS -> {
                    nav.popBackStack(NavigationRoute.PRODUCTS, inclusive = false)
                }

                NavigationRoute.DOCS -> {
                    nav.popBackStack(NavigationRoute.MAIN, inclusive = false)
                }

                else -> {
                    nav.popBackStack()
                }
            }

            if (!popSuccess) {
                when (source) {
                    NavigationRoute.PRODUCTS -> {
                        nav.navigate(NavigationRoute.PRODUCTS) {
                            popUpTo(NavigationRoute.MAIN) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    NavigationRoute.DOCS -> {
                        nav.navigate(NavigationRoute.MAIN) {
                            popUpTo(NavigationRoute.MAIN) {
                                inclusive = false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    else -> {
                        nav.navigate(NavigationRoute.MAIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        } ?: run {
            onBackClick()
        }
    }

    Scaffold { paddingValues ->
        product?.let { productData ->
            ProductDetailContent(
                product = productData,
                imagePath = imagePath,
                showEnglish = showEnglish,
                onLanguageToggle = { showEnglish = !showEnglish },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onHandleBackNavigation = handleBackNavigation
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
    modifier: Modifier = Modifier,
    onHandleBackNavigation: () -> Unit = { }
) {
    val finalImagePath = product.localImagePath ?: imagePath
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHandleBackNavigation) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = Constants.Product.PRODUCT_LABEL,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (product.information.indonesian.isNotEmpty() &&
                product.information.english.isNotEmpty()
            ) {
                IconButton(onClick = onLanguageToggle) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "“${product.productDestination}”",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = montserratFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                DetailAccordions(
                    title = product.productFullName,
                    product = product
                )
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            }

//        Spacer(modifier = Modifier.height(24.dp))

//        product.actionInfo.information.let { actionInfo ->
//            val actionText = remember(showEnglish, actionInfo) {
//                when {
//                    showEnglish && actionInfo.english.isNotBlank() ->
//                        actionInfo.english
//
//                    actionInfo.indonesian.isNotBlank() ->
//                        actionInfo.indonesian
//
//                    actionInfo.english.isNotBlank() ->
//                        actionInfo.english
//
//                    else -> null
//                }
//            }
//
//            actionText?.let { text ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(20.dp)
//                    ) {
//                        Text(
//                            text = "Additional Information",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.SemiBold,
//                            color = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.padding(bottom = 12.dp)
//                        )
//                        Text(
//                            text = text,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.onSurface,
//                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
//                        )
//                    }
//                }
//            }
//        }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    @Suppress("SpellCheckingInspection") val sampleProduct = Product(
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
            indonesian = "Indonesia Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun.",
            english = "English Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun. Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an, saat seorang tukang cetak yang tidak dikenal mengambil sebuah kumpulan teks dan mengacaknya untuk membuat sebuah buku contoh huruf. Ia tidak hanya bertahan selama 5 abad, tapi juga telah beralih ke penataan huruf elektronik, tanpa ada perubahan apapun."
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