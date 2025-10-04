package com.arkhe.menu.presentation.screen.docs.categories.screen

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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import com.arkhe.menu.utils.Constants.CurrentLanguage.INDONESIAN
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import org.koin.androidx.compose.koinViewModel

@Composable
fun HeaderAccordions(
    title: String,
    categoryViewModel: CategoryViewModel = koinViewModel()
) {
    val actionInfoEnText = categoryViewModel.getActionInfo(ENGLISH)
    val actionInfoIdText = categoryViewModel.getActionInfo(INDONESIAN)
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
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 50.dp),
            thickness = 2.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    }
}