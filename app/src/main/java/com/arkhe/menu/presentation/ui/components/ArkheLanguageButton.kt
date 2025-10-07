package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.LanguageModels
import com.arkhe.menu.domain.model.Languages
import com.arkhe.menu.presentation.ui.theme.ArkheTheme

@Composable
fun ArkheLanguageButton(
    selectedLanguage: LanguageModels,
    onLanguageSelected: (LanguageModels) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(Languages.availableLanguages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = language.code == selectedLanguage.code,
                    onLanguageSelected = onLanguageSelected
                )
            }
        }
    }
}

@Composable
private fun LanguageItem(
    language: LanguageModels,
    isSelected: Boolean,
    onLanguageSelected: (LanguageModels) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = if (isSelected) {
        ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            contentColor = MaterialTheme.colorScheme.primary
        )
    } else {
        ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }

    val buttonModifier = if (isSelected) {
        modifier
            .width(168.dp)
            .height(58.dp)
    } else {
        modifier
            .width(160.dp)
            .height(50.dp)
    }

    if (isSelected) {
        FilledTonalButton(
            onClick = { onLanguageSelected(language) },
            modifier = buttonModifier,
            colors = colors,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(start = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = language.nativeName,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = language.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.contentColor.copy(alpha = 0.7f)

                )
            }
        }
    } else {
        OutlinedButton(
            onClick = { onLanguageSelected(language) },
            modifier = buttonModifier,
            colors = colors,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(start = 16.dp)

        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = language.nativeName,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = language.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArkheLanguageButtonPreview() {
    ArkheTheme {
        ArkheLanguageButton(
            selectedLanguage = Languages.ENGLISH,
            onLanguageSelected = {}
        )
    }
}