package com.arkhe.menu.presentation.screen.docs.categories

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.presentation.screen.docs.categories.ext.CategoryItem
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("FrequentlyChangingValue")
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = koinViewModel(),
    onNavigateToDetail: () -> Unit = {}
) {
    /*✅ Using the State from BaseViewModel*/
    val categoriesState by categoryViewModel.state.collectAsState()

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = categoryViewModel.getScrollPosition()
    )

    /*✅ Improved refresh state management*/
    var isUserRefreshing by remember { mutableStateOf(false) }

    /*✅ Smart refresh indicator - show loading only user trigger refresh*/
    val showRefreshLoading by remember {
        derivedStateOf {
            isUserRefreshing && categoriesState is SafeApiResult.Loading
        }
    }

    /*✅ Auto-initialization with offline-first approach*/
    LaunchedEffect(Unit) {
        categoryViewModel.ensureDataLoaded()
    }

    /*✅ Reset refresh flag when not loading*/
    LaunchedEffect(categoriesState) {
        if (categoriesState !is SafeApiResult.Loading) {
            isUserRefreshing = false
        }
    }

    /*✅ Save scroll position when dispose*/
    LaunchedEffect(listState.firstVisibleItemIndex) {
        categoryViewModel.updateScrollPosition(listState.firstVisibleItemIndex)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isUserRefreshing = true
                    categoryViewModel.refreshCategories()
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                if (showRefreshLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Categories",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Category Tripkeun",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (categoriesState) {
                is SafeApiResult.Loading -> {
                    /*✅ Only show initial loading, not refresh loading*/
                    if (!isUserRefreshing) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Loading categories...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                is SafeApiResult.Success -> {
                    val categories = (categoriesState as SafeApiResult.Success<List<Category>>).data

                    if (categories.isEmpty()) {
                        /*✅ Empty state with better UX*/
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No categories found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Pull to refresh or tap the refresh button",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        isUserRefreshing = true
                                        categoryViewModel.refresh()
                                    }
                                ) {
                                    Text("Refresh")
                                }
                            }
                        }
                    } else {
                        /*✅ Success state with data*/
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = categories,
                                key = { it.id }
                            ) { category ->
                                CategoryItem(
                                    category = category,
                                    onClick = {
                                        categoryViewModel.selectCategory(category)
                                        onNavigateToDetail()
                                    }
                                )
                            }
                        }
                    }
                }

                is SafeApiResult.Error -> {
                    /*✅ Error state with better error handling*/
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Unable to load categories",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )

                            val errorMessage = (categoriesState as SafeApiResult.Error).exception.message
                            if (!errorMessage.isNullOrBlank()) {
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
                                )
                            }

                            Text(
                                text = "Check your internet connection and try again",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    isUserRefreshing = true
                                    categoryViewModel.refresh()
                                }
                            ) {
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}