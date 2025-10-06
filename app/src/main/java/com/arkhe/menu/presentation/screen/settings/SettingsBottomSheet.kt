@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.settings

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.presentation.screen.settings.account.AccountItem
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Shield
import compose.icons.evaicons.outline.Smartphone

@Composable
fun SettingsBottomSheet() {
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
            Spacer(Modifier.width(48.dp))
            HeaderTitleSecondary(
                title = stringResource(R.string.profile_settings)
            )
            Spacer(Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Didik Muttaqien",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "230504",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = sourceCodeProFontFamily
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                AccountItem(
                    label = "Personal Information",
                    labelInfo = "Name, Phone & Profile",
                    icon = EvaIcons.Outline.Person,
                    onClick = {},
                    showDivider = true
                )
                AccountItem(
                    label = "Sign-in & Security",
                    labelInfo = "Email, Password & PIN",
                    icon = EvaIcons.Outline.Shield,
                    onClick = {},
                    showDivider = true
                )
                AccountItem(
                    label = "Devices",
                    labelInfo = "Keep track of your devices",
                    icon = EvaIcons.Outline.Smartphone,
                    onClick = {},
                    showDivider = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserBottomSheetPreview() {
    AppTheme {
        SettingsBottomSheet()
    }
}