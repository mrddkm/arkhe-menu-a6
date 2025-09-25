package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ProductStatistics
import com.arkhe.menu.presentation.screen.docs.categories.content.StatisticItem
import com.arkhe.menu.presentation.ui.components.HeaderLabel
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import com.arkhe.menu.utils.Constants.CurrentLanguage.INDONESIAN
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_TITLE
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_TOTAL
import com.arkhe.menu.utils.getDevelopmentColor
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HeaderAccordions(
    title: String,
    productViewModel: ProductViewModel = koinViewModel()
) {
    val actionInfoEnText = productViewModel.getActionInfo(ENGLISH)
    val actionInfoIdText = productViewModel.getActionInfo(INDONESIAN)
    var showEnglish by remember { mutableStateOf(false) }
    LazyColumn {
        item {
            var expanded by remember { mutableStateOf(false) }
            val degrees by animateFloatAsState(if (expanded) -90f else 90f)
            Column {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (expanded) {
                            if (actionInfoEnText.isNotEmpty() && actionInfoIdText.isNotEmpty()
                            ) {
                                IconButton(
                                    onClick = { showEnglish = !showEnglish }
                                ) {
                                    if (showEnglish) {
                                        LanguageIconEn()
                                    } else {
                                        LanguageIconId()
                                    }
                                }
                            }
                        }
                        IconButton(
                            onClick = { expanded = expanded.not() }
                        ) {
                            Icon(
                                imageVector = EvaIcons.Outline.ArrowIosForward,
                                contentDescription = null,
                                modifier = Modifier
                                    .rotate(degrees),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(
                        spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntSize.VisibilityThreshold
                        )
                    ),
                    exit = shrinkVertically()
                ) {
                    AnimatedVisibilityContent(
                        actionInfoEnText = actionInfoEnText,
                        actionInfoIdText = actionInfoIdText,
                        stats = productViewModel.getProductStatistics(),
                        showEnglish = showEnglish
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedVisibilityContent(
    actionInfoEnText: String = "",
    actionInfoIdText: String = "",
    stats: ProductStatistics? = null,
    showEnglish: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val informationText = remember(showEnglish, actionInfoEnText, actionInfoIdText) {
                when {
                    showEnglish && actionInfoEnText.isNotBlank() ->
                        actionInfoEnText

                    actionInfoIdText.isNotBlank() ->
                        actionInfoIdText

                    actionInfoEnText.isNotBlank() ->
                        actionInfoEnText

                    else -> "No information available"
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = informationText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderLabel(STATISTICS_TITLE)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticItem(
                        label = STATISTICS_TOTAL,
                        value = stats?.total.toString(),
                        color = getDevelopmentColor(STATISTICS_TOTAL)
                    )
                    StatisticItem(
                        label = STATISTICS_READY,
                        value = stats?.ready.toString(),
                        color = getDevelopmentColor(STATISTICS_READY)
                    )
                    StatisticItem(
                        label = STATISTICS_RESEARCH,
                        value = stats?.research.toString(),
                        color = getDevelopmentColor(STATISTICS_RESEARCH)
                    )
                    StatisticItem(
                        label = STATISTICS_INITIATION,
                        value = stats?.initiation.toString(),
                        color = getDevelopmentColor(STATISTICS_INITIATION)
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 50.dp),
            thickness = 2.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedVisibilityContentPreview() {
    AppTheme {
        AnimatedVisibilityContent()
    }
}
