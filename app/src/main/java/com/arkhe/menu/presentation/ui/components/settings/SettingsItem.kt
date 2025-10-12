package com.arkhe.menu.presentation.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.components.CustomToggle
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Edit2

@Composable
fun SettingsItem(
    label: String? = null,
    labelInfo: String? = null,
    value: String? = null,
    showIcon: Boolean = true,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 12.dp, bottom = 0.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
                value?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                labelInfo?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            if (showIcon) {
                Icon(
                    imageVector = EvaIcons.Outline.Edit2,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 20.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    } else Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun SettingsToggleItem(
    label: String? = null,
    value: String? = null,
    info: String? = null,
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
    showDivider: Boolean = true
) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 12.dp, bottom = 0.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                label?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
                value?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                info?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            CustomToggle(
                isActive = isActive,
                onToggle = onToggle
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 20.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )
    } else Spacer(modifier = Modifier.height(4.dp))
}

@Preview(showBackground = true)
@Composable
fun AccountToggleItemPreview() {
    ArkheTheme {
        Column {
            SettingsToggleItem(
                value = "Biometric",
                showDivider = false,
                isActive = false,
                onToggle = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountEditItemPreview() {
    ArkheTheme {
        Column {
            SettingsItem(
                label = "Name",
                value = "DIDIK MUTTAQIEN",
                labelInfo = "Use your real name",
                onClick = {},
                showDivider = true
            )
            SettingsItem(
                label = "Initial/Nick Name",
                value = "DM - mrddkm",
                onClick = {},
            )
        }
    }
}