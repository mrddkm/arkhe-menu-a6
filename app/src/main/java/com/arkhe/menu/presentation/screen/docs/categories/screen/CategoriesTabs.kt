package com.arkhe.menu.presentation.screen.docs.categories.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.arkhe.menu.R
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.formatItemCount
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Droplet
import compose.icons.evaicons.outline.Pin

@Composable
fun CategoriesTabs(
    navController: NavController
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        stringResource(R.string.category) to EvaIcons.Fill.Droplet,
        stringResource(R.string.type) to EvaIcons.Outline.Pin
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .height(40.dp),
            indicator = {}
        ) {
            tabs.forEachIndexed { index, (title, icon) ->
                val selected = selectedTabIndex == index
                Tab(
                    selected = selected,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier
                        .background(
                            if (selectedTabIndex == index) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.3f
                            )
                            else MaterialTheme.colorScheme.surface
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (selected) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                            )
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (selectedTabIndex) {
            0 -> CategoriesTabContent(navController)
            1 -> TypeTabContent(navController)
        }
    }
}

@Composable
private fun CategoriesTabContent(
    navController: NavController
) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val categories by categoryViewModel.categoriesState.collectAsState()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val categoryNames = when (categories) {
        is SafeApiResult.Success -> categoryViewModel.getCategoryNames()
        else -> emptyList()
    }

    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                categoryNames.forEachIndexed { index, categoryName ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            productViewModel.filterProductsByCategoryName(categoryNames[index].name)
                        },
                        text = {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                            ) {
                                Icon(
                                    imageVector = EvaIcons.Fill.Droplet,
                                    contentDescription = null,
                                    tint = parseColorFromHex(categoryName.colors.iconColor),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    )
                }
            }
            val filteredProducts by productViewModel.filteredProducts.collectAsState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = categoryNames[selectedTabIndex].name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = formatItemCount(filteredProducts.size),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(filteredProducts) { product ->
                    CategoriesProductsItems(
                        product = product,
                        onClick = {
                            navController.navigate(
                                NavigationRoute.productDetail(
                                    productId = product.id,
                                    source = NavigationRoute.CATEGORIES
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeTabContent(
    navController: NavController
) {
    val categoriesViewModel: CategoryViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val categories by categoriesViewModel.categoriesState.collectAsState()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val categoryTypes = when (categories) {
        is SafeApiResult.Success -> categoriesViewModel.getCategoryTypes()
        else -> emptyList()
    }

    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                categoryTypes.forEachIndexed { index, categoryType ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            productViewModel.filterProductsByCategoryType(categoryTypes[index])
                        },
                        text = {
                            Text(
                                text = categoryType,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selectedTabIndex == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                            )
                        }
                    )
                }
            }
            val filteredProducts by productViewModel.filteredProducts.collectAsState()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = formatItemCount(filteredProducts.size),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(filteredProducts) { product ->
                    CategoriesProductsItems(
                        product = product,
                        onClick = {
                            navController.navigate(
                                NavigationRoute.productDetail(
                                    productId = product.id,
                                    source = NavigationRoute.CATEGORIES
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}