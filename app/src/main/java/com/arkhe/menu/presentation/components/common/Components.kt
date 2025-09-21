@file:Suppress("SpellCheckingInspection")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.arkhe.menu.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import com.arkhe.menu.utils.getDevelopmentColor
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.MoreHorizontal
import compose.icons.evaicons.outline.ArrowIosForward

@Composable
fun HeaderContent(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
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
            .padding(start = paddingStart, top = 4.dp, end = 8.dp, bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = EvaIcons.Outline.ArrowIosForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun MoreSection(
    onMoreClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMoreClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
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
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.MoreHorizontal,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "More",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray.copy(alpha = 1.5f)
                )
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun HeaderBottomSheet(
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
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

@Preview(showBackground = true)
@Composable
fun HeaderScreenPreview() {
    AppTheme {
        HeaderContent(
            title = "Docs"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    AppTheme {
        HeaderSection(
            title = "Header Title",
            onHeaderClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoreSectionPreview() {
    AppTheme {
        MoreSection(onMoreClick = {})
    }
}