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
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.ui.components.FeedSection
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.components.TimeMachineSection
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.montserratFontFamily
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.sampleProduct
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit,
    navController: NavController? = null,
    productViewModel: ProductViewModel = koinViewModel(
        key = "main_product_viewmodel"
    ),
    imagePath: String? = null
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var showEnglish by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        product = productViewModel.getProductById(productId)
    }

    val handleBackNavigation: () -> Unit = {
        navController?.let { nav ->
            val popSuccess = nav.popBackStack()
            if (!popSuccess) {
                nav.navigate(NavigationRoute.MAIN) {
                    popUpTo(NavigationRoute.MAIN) {
                        inclusive = true
                    }
                    launchSingleTop = true
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
                text = stringResource(R.string.product),
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
                                placeholder = painterResource(R.drawable.ic_image),
                                error = painterResource(R.drawable.ic_alert_triangle),
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_image),
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
                            placeholder = painterResource(R.drawable.ic_image),
                            error = painterResource(R.drawable.ic_alert_triangle),
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.ic_image),
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
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeedSection { }
                TimeMachineSection()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    AppTheme {
        ProductDetailContent(
            product = sampleProduct,
            imagePath = null,
            showEnglish = false,
            onLanguageToggle = {}
        )
    }
}