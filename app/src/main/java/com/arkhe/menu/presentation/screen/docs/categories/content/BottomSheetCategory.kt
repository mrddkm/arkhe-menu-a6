@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.categories.content

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryActionInfo
import com.arkhe.menu.domain.model.CategoryColors
import com.arkhe.menu.domain.model.CategoryInformationLanguage
import com.arkhe.menu.presentation.screen.docs.categories.screen.parseColorFromHex
import com.arkhe.menu.presentation.ui.components.HeaderLabel
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.components.MoreSection
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_TITLE
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_TOTAL
import com.arkhe.menu.utils.getDevelopmentColor
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Droplet

@Composable
fun BottomSheetCategory(
    category: Category
) {
    var showEnglish by remember { mutableStateOf(false) }

    val backgroundColor = parseColorFromHex(category.colors.backgroundColor)
    val iconColor = parseColorFromHex(category.colors.iconColor)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (category.information.indonesian.isNotEmpty() &&
                category.information.english.isNotEmpty()
            ) {
                Spacer(Modifier.width(48.dp))
            }
            HeaderTitleSecondary(
                title = stringResource(R.string.category)
            )
            if (category.information.indonesian.isNotEmpty() && category.information.english.isNotEmpty()
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
            } else {
                Spacer(Modifier.width(48.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.Droplet,
                    contentDescription = category.name,
                    modifier = Modifier.size(42.dp),
                    tint = iconColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = category.type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeaderLabel(
                    label = STATISTICS_TITLE
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticItem(
                        label = STATISTICS_TOTAL,
                        value = category.productCount.toString(),
                        color = getDevelopmentColor(STATISTICS_TOTAL)
                    )
                    StatisticItem(
                        label = STATISTICS_READY,
                        value = category.ready.toString(),
                        color = getDevelopmentColor(STATISTICS_READY)
                    )
                    StatisticItem(
                        label = STATISTICS_RESEARCH,
                        value = category.research.toString(),
                        color = getDevelopmentColor(STATISTICS_RESEARCH)
                    )
                    StatisticItem(
                        label = STATISTICS_INITIATION,
                        value = category.initiation.toString(),
                        color = getDevelopmentColor(STATISTICS_INITIATION)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        val informationText = remember(showEnglish, category.information) {
            when {
                showEnglish && category.information.english.isNotBlank() ->
                    category.information.english

                category.information.indonesian.isNotBlank() ->
                    category.information.indonesian

                category.information.english.isNotBlank() ->
                    category.information.english

                else -> "No information available"
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = informationText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        MoreSection(onMoreClick = {})
    }
}

@Composable
fun StatisticItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = sourceCodeProFontFamily,
                fontWeight = FontWeight.Normal
            ),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetCategoryPreview() {
    val sampleCategory = Category(
        id = "SRS",
        name = "Series",
        type = "Regular",
        productCount = 26,
        initiation = 0,
        research = 0,
        ready = 26,
        information = CategoryInformationLanguage(
            indonesian = "Lorem Ipsum adalah contoh teks atau dummy dalam industri percetakan dan penataan huruf atau typesetting. Lorem Ipsum telah menjadi standar contoh teks sejak tahun 1500an",
            english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
        ),
        colors = CategoryColors(
            backgroundColor = "0xFFE0F2F1",
            iconColor = "0xFF00695C"
        ),
        actionInfo = CategoryActionInfo(
            action = "productcategory",
            information = CategoryInformationLanguage(
                indonesian = "Lorem Ipsum hanyalah contoh teks dalam industri percetakan dan penataan huruf. Lorem Ipsum telah menjadi contoh teks standar industri sejak tahun 1500-an.",
                english = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Sit amet consectetur adipiscing elit quisque faucibus ex. Adipiscing elit quisque faucibus ex sapien vitae pellentesque."
            )
        )
    )
    AppTheme {
        BottomSheetCategory(
            category = sampleCategory
        )
    }
}