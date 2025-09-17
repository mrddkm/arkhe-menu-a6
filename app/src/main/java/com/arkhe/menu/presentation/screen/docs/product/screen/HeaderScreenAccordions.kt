package com.arkhe.menu.presentation.screen.docs.product.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.presentation.screen.docs.categories.content.StatisticItem
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.utils.Constants
import com.arkhe.menu.utils.Constants.Category.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Category.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Category.STATISTICS_RESEARCH
import com.arkhe.menu.utils.Constants.Category.STATISTICS_TOTAL
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import com.arkhe.menu.utils.Constants.CurrentLanguage.INDONESIAN
import com.arkhe.menu.utils.getDevelopmentColor
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.Globe
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HeaderScreenAccordions(
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
                        color = MaterialTheme.colorScheme.primary
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
                                        Icon(
                                            imageVector = EvaIcons.Outline.Globe,
                                            contentDescription = "Toggle Language English",
                                            modifier = Modifier.size(24.dp),
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(R.drawable.ic_id_indonesia),
                                            contentDescription = "Toggle Language Indonesia",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .border(
                                                    0.5.dp,
                                                    Color.LightGray,
                                                    shape = CircleShape
                                                ),
                                        )
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
                                tint = Color.Gray
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
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
        }
        Column(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = Constants.Category.STATISTICS_LABEL,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticItem(
                    label = STATISTICS_TOTAL,
                    value = "10",
                    color = getDevelopmentColor(STATISTICS_TOTAL)
                )
                StatisticItem(
                    label = STATISTICS_READY,
                    value = "10",
                    color = getDevelopmentColor(STATISTICS_READY)
                )
                StatisticItem(
                    label = STATISTICS_RESEARCH,
                    value = "10",
                    color = getDevelopmentColor(STATISTICS_RESEARCH)
                )
                StatisticItem(
                    label = STATISTICS_INITIATION,
                    value = "10",
                    color = getDevelopmentColor(STATISTICS_INITIATION)
                )
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
