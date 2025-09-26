package com.arkhe.menu.presentation.screen.docs.product.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ChevronRight

@Composable
fun DetailAccordions(
    title: String,
    product: Product
) {
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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = expanded.not() }
                            .padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = sourceCodeProFontFamily,
                                fontWeight = FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Icon(
                            imageVector = EvaIcons.Outline.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(degrees),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
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
                    AnimatedVisibilityTabContent(
                        product = product
                    )
                }
            }
        }
    }
}