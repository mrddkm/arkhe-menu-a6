@file:OptIn(ExperimentalMaterial3Api::class)

package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import com.arkhe.menu.utils.DistanceResult
import com.arkhe.menu.utils.getDevelopmentColor
import com.arkhe.menu.utils.toDistanceResult
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.MoreHorizontal
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.Camera
import compose.icons.evaicons.outline.Clock
import compose.icons.evaicons.outline.Globe

@Composable
fun HeaderContent(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
        )
    }
}

@Composable
fun HeaderSection(
    title: String,
    paddingStart: Dp = 0.dp,
    onHeaderClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, top = 16.dp, end = 8.dp, bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = { onHeaderClick() }
            ) {
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun HeaderTitleSecondary(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun HeaderLabel(
    label: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MoreSection(
    onMoreClick: () -> Unit
) {
    Surface(
        onClick = onMoreClick,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.MoreHorizontal,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.more),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun FeedSection(
    onFeedClick: () -> Unit
) {
    Surface(
        onClick = onFeedClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .width(170.dp)
                .padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Outline.Camera,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.feeds),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TimeMachineSection(
    onTimeMachineClick: () -> Unit
) {
    Surface(
        onClick = onTimeMachineClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .width(170.dp)
                .padding(start = 8.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Outline.Clock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.time_machine),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatusDevelopmentChip(
    status: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (status) {
                STATISTICS_READY -> getDevelopmentColor(STATISTICS_READY).copy(alpha = 0.1f)
                STATISTICS_RESEARCH -> getDevelopmentColor(STATISTICS_RESEARCH).copy(
                    alpha = 0.1f
                )

                STATISTICS_INITIATION -> getDevelopmentColor(STATISTICS_INITIATION).copy(
                    alpha = 0.1f
                )

                else -> Color.LightGray
            }
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = sourceCodeProFontFamily,
                fontWeight = FontWeight.Normal
            ),
            color = when (status) {
                STATISTICS_READY -> getDevelopmentColor(STATISTICS_READY)
                STATISTICS_RESEARCH -> getDevelopmentColor(STATISTICS_RESEARCH)
                STATISTICS_INITIATION -> getDevelopmentColor(STATISTICS_INITIATION)
                else -> Color.DarkGray
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun LanguageIconEn() {
    Icon(
        imageVector = EvaIcons.Outline.Globe,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun LanguageIconId() {
    Image(
        painter = painterResource(R.drawable.ic_id_indonesia),
        contentDescription = null,
        modifier = Modifier
            .size(22.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.primary.copy(0.3f), shape = CircleShape),
    )
}

@Composable
fun ProductInfoItem(
    label: String = "",
    value: String? = null,
    valueCompose: @Composable (() -> Unit)? = null,
    useHorizontalDivider: Boolean = true,
    maxLine: Int = 1
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, bottom = 6.dp)
    ) {
        if (useHorizontalDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = Color.Gray
            )
            Box(
                modifier = Modifier.padding(end = 24.dp)
            ) {
                if (value != null) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = sourceCodeProFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = maxLine,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (valueCompose != null) {
                    valueCompose()
                }
            }
        }
    }
}

@Composable
fun ProductDestinationItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: Painter,
    isParser: Boolean = true,
    isMdpl: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = Color.Gray
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = Color.Gray
            )
            if (isParser) {
                DistanceText(
                    distanceString = value,
                    isMdpl = isMdpl
                )
            } else {
                when (value) {
                    "00:00" -> {
                        Text(
                            text = "⎯".repeat(3),
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = sourceCodeProFontFamily
                            )
                        )
                    }

                    "-" -> {
                        Text(
                            text = "⎯".repeat(3),
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = sourceCodeProFontFamily
                            )
                        )
                    }

                    else -> {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = sourceCodeProFontFamily,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DistanceText(
    modifier: Modifier = Modifier,
    distanceString: String,
    isMdpl: Boolean = false,
    numberColor: Color = MaterialTheme.colorScheme.onSurface,
    unitColor: Color = Color.Gray,
    messageColor: Color = Color.Red,
    placeholderColor: Color = Color.Gray
) {
    when (val result = distanceString.toDistanceResult(isMdpl)) {
        is DistanceResult.Hidden -> {
            Text(
                text = "⎯".repeat(3),
                color = placeholderColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = sourceCodeProFontFamily
                ),
                modifier = modifier
            )
        }

        is DistanceResult.Incomplete -> {
            Text(
                text = "⎯".repeat(3),
                color = messageColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = sourceCodeProFontFamily
                ),
                modifier = modifier
            )
        }

        is DistanceResult.Value -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = result.number,
                    color = numberColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = sourceCodeProFontFamily
                )
                Text(
                    text = result.unit,
                    color = unitColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun StatusDevelopmentChipPreview() {
    AppTheme {
        ProductDestinationItem(
            label = "Altitude",
            value = sampleProduct.hikeAltitude,
            icon = painterResource(R.drawable.ic_elevation)
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun ProductInfoItemPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProductInfoItem(
                    label = "Status",
                    valueCompose = { StatusDevelopmentChip(sampleProduct.status) },
                    useHorizontalDivider = false
                )
                ProductInfoItem(
                    label = "Code",
                    value = sampleProduct.productCode,
                    useHorizontalDivider = true
                )
                ProductInfoItem(
                    label = "Category",
                    value = sampleProduct.categoryName,
                    useHorizontalDivider = true
                )
            }
        }
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun HeaderScreenPreview() {
    AppTheme {
        HeaderContent(
            title = stringResource(R.string.docs)
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    AppTheme {
        HeaderSection(
            title = stringResource(R.string.categories),
            onHeaderClick = {}
        )
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun MoreSectionPreview() {
    AppTheme {
        MoreSection(onMoreClick = {})
    }
}*/

@Preview(showBackground = true)
@Composable
fun FeedSectionPreview() {
    AppTheme {
        FeedSection(onFeedClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TimeMachineSectionPreview() {
    AppTheme {
        TimeMachineSection(onTimeMachineClick = {})
    }
}