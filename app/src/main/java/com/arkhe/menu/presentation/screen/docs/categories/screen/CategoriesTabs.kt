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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.FileText
import compose.icons.evaicons.outline.Pin

@Composable
fun CategoriesTabs() {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        "Categories" to EvaIcons.Outline.FileText,
        "Type" to EvaIcons.Outline.Pin
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
            0 -> CategoriesTabContent()
            1 -> TypeTabContent()
        }
    }
}

@Composable
private fun CategoriesTabContent() {
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
            CategoriesTabItems()
        }
    }
}

@Composable
fun CategoriesTabItems(viewModel: CategoryViewModel = viewModel()) {
    val categories by viewModel.categoriesState.collectAsState()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val categoryNames = when (categories) {
        is SafeApiResult.Success -> viewModel.getCategoryNames()
        else -> emptyList()
    }

    Column {
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
                    onClick = { selectedTabIndex = index },
                    text = {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(parseColorFromHex(categoryName.colors.iconColor))
                        )
                    }
                )
            }
        }
        if (categoryNames.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = categoryNames[selectedTabIndex].name,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun TypeTabContent(viewModel: CategoryViewModel = viewModel()) {
    val categories by viewModel.categoriesState.collectAsState()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val categoryTypes = when (categories) {
        is SafeApiResult.Success -> viewModel.getCategoryTypes()
        else -> emptyList()
    }

    Column {
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
                    onClick = { selectedTabIndex = index },
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
        if (categoryTypes.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = categoryTypes[selectedTabIndex],
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}