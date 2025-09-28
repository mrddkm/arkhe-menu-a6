package com.arkhe.menu.presentation.screen.docs.product.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.ui.components.ErrorIncompleteData
import com.arkhe.menu.presentation.ui.components.ProductDestinationItem
import com.arkhe.menu.presentation.ui.components.ProductInfoItem
import com.arkhe.menu.presentation.ui.components.StatusDevelopmentChip
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.utils.sampleProduct
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.FileText
import compose.icons.evaicons.outline.Info
import compose.icons.evaicons.outline.Pin

@Composable
fun AnimatedVisibilityTabContent(
    product: Product
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        "Product" to EvaIcons.Outline.FileText,
        "Destination" to EvaIcons.Outline.Pin
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
            0 -> ProductScreen(product)
            1 -> DestinationScreen(product)
        }
    }
}

@Composable
fun ProductScreen(
    product: Product
) {
    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ProductInfoItem(
                label = "Status",
                valueCompose = { StatusDevelopmentChip(product.status) },
                useHorizontalDivider = false
            )
            ProductInfoItem(
                label = "Code",
                value = product.productCode,
                useHorizontalDivider = true
            )
            ProductInfoItem(
                label = "Category",
                value = product.categoryName,
                useHorizontalDivider = true
            )
            ProductInfoItem(
                label = "Type",
                value = product.categoryType,
                useHorizontalDivider = true
            )
            ProductInfoItem(
                label = "Tagline",
                value = product.productTagLine,
                useHorizontalDivider = true,
                maxLine = 2
            )
        }
    }
}

@Composable
fun DestinationScreen(
    product: Product
) {
    Surface(
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Level",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = product.hikeLevelName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = EvaIcons.Outline.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductDestinationItem(
                        modifier = Modifier.weight(1f),
                        label = "Distance",
                        value = product.hikeDistance,
                        icon = painterResource(R.drawable.ic_distance)
                    )
                    ProductDestinationItem(
                        modifier = Modifier.weight(1f),
                        label = "Duration",
                        value = product.hikeDuration,
                        icon = painterResource(R.drawable.ic_duration),
                        isParser = false
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductDestinationItem(
                        modifier = Modifier.weight(1f),
                        label = "Altitude (m)",
                        value = product.hikeAltitude,
                        icon = painterResource(R.drawable.ic_altitude),
                        isMdpl = true
                    )
                    ProductDestinationItem(
                        modifier = Modifier.weight(1f),
                        label = "Elevation Gain",
                        value = product.hikeElevationGain,
                        icon = painterResource(R.drawable.ic_elevation)
                    )
                }
            }
            ErrorIncompleteData(product)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedTabsScreenPreview() {
    AppTheme {
        DestinationScreen(
            sampleProduct
        )
    }
}
