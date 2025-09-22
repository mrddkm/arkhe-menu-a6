package com.arkhe.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.PRIMARY
) {
    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors()
        ButtonVariant.SECONDARY -> ButtonDefaults.outlinedButtonColors()
        ButtonVariant.TERTIARY -> ButtonDefaults.textButtonColors()
    }

    when (variant) {
        ButtonVariant.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                colors = colors
            ) {
                ButtonContent(text = text, icon = icon)
            }
        }
        ButtonVariant.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                colors = colors
            ) {
                ButtonContent(text = text, icon = icon)
            }
        }
        ButtonVariant.TERTIARY -> {
            TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                colors = colors
            ) {
                ButtonContent(text = text, icon = icon)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }
        Text(text)
    }
}

enum class ButtonVariant {
    PRIMARY,
    SECONDARY,
    TERTIARY
}