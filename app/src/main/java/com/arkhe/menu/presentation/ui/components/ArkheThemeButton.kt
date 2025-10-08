package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ThemeModels
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Moon
import compose.icons.evaicons.outline.Smartphone
import compose.icons.evaicons.outline.Sun

@Composable
fun ArkheThemeButtons(
    currentTheme: ThemeModels,
    onThemeSelected: (ThemeModels) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ThemeIconButton(
                themeMode = ThemeModels.LIGHT,
                isSelected = currentTheme == ThemeModels.LIGHT,
                onClick = {
                    onThemeSelected(ThemeModels.LIGHT)
                }
            )

            ThemeIconButton(
                themeMode = ThemeModels.SYSTEM,
                isSelected = currentTheme == ThemeModels.SYSTEM,
                onClick = {
                    onThemeSelected(ThemeModels.SYSTEM)
                }
            )

            ThemeIconButton(
                themeMode = ThemeModels.DARK,
                isSelected = currentTheme == ThemeModels.DARK,
                onClick = {
                    onThemeSelected(ThemeModels.DARK)
                }
            )
        }
    }
}

@Composable
private fun ThemeIconButton(
    themeMode: ThemeModels,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (themeMode) {
        ThemeModels.LIGHT -> EvaIcons.Outline.Sun
        ThemeModels.DARK -> EvaIcons.Outline.Moon
        ThemeModels.SYSTEM -> EvaIcons.Outline.Smartphone
    }

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
        modifier.size(68.dp)
    } else {
        modifier.size(60.dp)
    }

    if (isSelected) {
        FilledTonalButton(
            onClick = onClick,
            modifier = buttonModifier,
            colors = colors,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = themeMode.displayName,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (themeMode) {
                        ThemeModels.LIGHT -> "Light"
                        ThemeModels.DARK -> "Dark"
                        ThemeModels.SYSTEM -> "System"
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            colors = colors,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = themeMode.displayName,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (themeMode) {
                        ThemeModels.LIGHT -> "Light"
                        ThemeModels.DARK -> "Dark"
                        ThemeModels.SYSTEM -> "System"
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThreeButtonsRowPreview() {
    ArkheTheme {
        ArkheThemeButtons(
            currentTheme = ThemeModels.LIGHT,
            onThemeSelected = {}
        )
    }
}