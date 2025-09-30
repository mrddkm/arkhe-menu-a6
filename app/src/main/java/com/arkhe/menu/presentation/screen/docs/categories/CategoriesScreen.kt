package com.arkhe.menu.presentation.screen.docs.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.R
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoryItem
import com.arkhe.menu.presentation.screen.docs.categories.screen.CategoriesTabs
import com.arkhe.menu.presentation.screen.docs.categories.screen.HeaderAccordions
import com.arkhe.menu.presentation.ui.components.LoadingIndicatorSpinner
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("FrequentlyChangingValue")
@Composable
fun CategoriesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = koinViewModel(),
    onNavigateToDetail: () -> Unit = {}
) {
    val categoriesState by categoryViewModel.categoriesState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val lastSuccess = remember { mutableStateOf<List<Category>?>(null) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = categoryViewModel.getScrollPosition()
    )

    LaunchedEffect(Unit) {
        categoryViewModel.ensureDataLoaded()
    }

    LaunchedEffect(categoriesState) {
        if (categoriesState !is SafeApiResult.Loading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(categoriesState) {
        if (categoriesState is SafeApiResult.Success) {
            lastSuccess.value = (categoriesState as SafeApiResult.Success<List<Category>>).data
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex) {
        categoryViewModel.updateScrollPosition(listState.firstVisibleItemIndex)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        when (categoriesState) {
            is SafeApiResult.Loading -> {
                LoadingIndicatorSpinner(
                    message = stringResource(R.string.categories)
                )
            }

            is SafeApiResult.Error -> {
                if (!lastSuccess.value.isNullOrEmpty()) {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = lastSuccess.value as List<Any?>,
                            key = { it.hashCode() }
                        ) { category ->
                            CategoryItem(
                                category = lastSuccess.value!!.first(),
                                onClick = {
                                    categoryViewModel.selectCategory(lastSuccess.value!!.first())
                                    onNavigateToDetail()
                                }
                            )
                        }
                    }
                    Text(
                        text = "Failed sync, displaying old data",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = "Failed to load categories",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )

                            (categoriesState as SafeApiResult.Error).exception.message?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    isRefreshing = true
                                    categoryViewModel.refreshCategories()
                                },
                                enabled = !isRefreshing
                            ) {
                                Text(if (isRefreshing) "Retrying..." else "Try Again")
                            }
                        }
                    }
                }
            }

            is SafeApiResult.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeaderAccordions(
                        title = stringResource(R.string.categories)
                    )
                }
                CategoriesTabs(navController)
            }
        }
    }
}