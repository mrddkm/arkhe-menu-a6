package com.arkhe.menu.presentation.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosForward
import compose.icons.evaicons.outline.Image

@Composable
fun SettingsBottomSheetItem(
    label: String = "",
    labelInfo: String? = null,
    icon: ImageVector? = null,
    painter: Painter? = null,
    showDivider: Boolean = true,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(start = 16.dp, top = 12.dp, bottom = 0.dp, end = 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (painter != null) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = EvaIcons.Outline.Image,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    labelInfo?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Icon(
                    imageVector = EvaIcons.Outline.ArrowIosForward,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 32.dp + 12.dp),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
        } else Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AccountItemPreview() {
    ArkheTheme {
        SettingsBottomSheetItem(
            label = "Personal Info",
            labelInfo = "Name, Email, Phone Number",
            icon = EvaIcons.Outline.Image,
            onClick = {}
        )
    }
}