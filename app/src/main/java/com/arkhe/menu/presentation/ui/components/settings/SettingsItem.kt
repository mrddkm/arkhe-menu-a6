package com.arkhe.menu.presentation.ui.components.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.arkhe.menu.R
import com.arkhe.menu.presentation.ui.components.CustomToggle
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Activity
import compose.icons.evaicons.outline.Edit2
import compose.icons.evaicons.outline.Refresh

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

@Composable
fun SettingsPhotoProfileItem(
    label: String,
    imageUri: String? = null,
    onChangePhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 12.dp, bottom = 12.dp, end = 20.dp)
            .clickable { onChangePhotoClick() },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.weight(0.75f)
            )
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .size(64.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    model = imageUri,
                    error = painterResource(id = R.drawable.ic_alert_triangle),
                    placeholder = painterResource(id = R.drawable.ic_bitrise)
                )

                if (imageUri.isNullOrEmpty()) {
                    Image(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        alpha = 0.9f
                    )
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoProfileBottomSheet(
    imageUri: String?,
    onDismiss: () -> Unit,
    onChooseFromGallery: () -> Unit,
    onRemovePhoto: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Picture",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .clickable {
                    onChooseFromGallery()
                    onDismiss()
                },
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(
                model = imageUri,
                error = painterResource(id = R.drawable.ic_alert_triangle),
                placeholder = painterResource(id = R.drawable.ic_bitrise)
            )

            if (imageUri.isNullOrEmpty()) {
                Image(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Default photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    alpha = 0.9f
                )
            } else {
                Image(
                    painter = painter,
                    contentDescription = "Profile photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }
        }

        Button(
            onClick = {
                onRemovePhoto()
                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .width(180.dp)
                .height(40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = EvaIcons.Outline.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Remove Photo",
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun PhotoProfile(
    imageUri: String?,
    isDefault: Boolean = true,
    size: Dp = 64.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageUri,
            error = painterResource(id = R.drawable.ic_alert_triangle),
            placeholder = painterResource(id = R.drawable.ic_bitrise)
        )

        if (imageUri.isNullOrEmpty()) {
            if (isDefault) {
                Image(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    alpha = 0.9f
                )
            } else {
                Image(
                    imageVector = EvaIcons.Outline.Activity,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    alpha = 0.9f
                )
            }
        } else {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun SettingsPhotoProfileItemPreview() {
    ArkheTheme {
        SettingsPhotoProfileItem(
            label = "A picture helps people recognize you and when you’re signed in.",
            imageUri = null,
            onChangePhotoClick = {}
        )
    }
}*/

@Preview(showBackground = true)
@Composable
fun PhotoProfileBottomSheetPreview() {
    ArkheTheme {
        PhotoProfileBottomSheet(
            imageUri = null,
            onDismiss = {},
            onChooseFromGallery = {},
            onRemovePhoto = {}
        )
    }
}

/*@Preview(showBackground = true)
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
}*/

/*
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
}*/
