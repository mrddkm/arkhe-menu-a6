package com.arkhe.menu.presentation.screen.docs.profile.ext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.utils.PhoneNumberParser
import com.arkhe.menu.utils.formatToInternationalWithDash
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Googlemaps
import compose.icons.simpleicons.Instagram
import compose.icons.simpleicons.Tiktok
import compose.icons.simpleicons.Whatsapp
import compose.icons.simpleicons.Youtube

@Composable
fun SocialMediaCard(
    socialMedia: SocialMedia,
    onSocialMediaClick: (String) -> Unit
) {
    val phoneFormatted = formatToInternationalWithDash(socialMedia.whatsApp)
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Find Us On",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.primary
        )

        if (socialMedia.googleMaps.isNotBlank()) {
            SocialMediaItem(
                icon = SimpleIcons.Googlemaps,
                value = "View Location",
                onClick = { onSocialMediaClick(socialMedia.googleMaps) }
            )
        }

        if (socialMedia.whatsApp.isNotBlank()) {
            SocialMediaItem(
                icon = SimpleIcons.Whatsapp,
                value = phoneFormatted,
                onClick = { onSocialMediaClick("https://wa.me/${socialMedia.whatsApp}") }
            )
        }

        if (socialMedia.instagram.isNotBlank()) {
            SocialMediaItem(
                icon = SimpleIcons.Instagram,
                value = socialMedia.instagram,
                onClick = { onSocialMediaClick("https://instagram.com/${socialMedia.instagram}") }
            )
        }

        if (socialMedia.tiktok.isNotBlank()) {
            SocialMediaItem(
                icon = SimpleIcons.Tiktok,
                value = socialMedia.tiktok,
                onClick = { onSocialMediaClick("https://tiktok.com/@${socialMedia.tiktok}") }
            )
        }

        if (socialMedia.youtube.isNotBlank()) {
            SocialMediaItem(
                icon = SimpleIcons.Youtube,
                value = socialMedia.youtube,
                onClick = { onSocialMediaClick("https://youtube.com/@${socialMedia.youtube}") }
            )
        }
    }
}

@Composable
fun SocialMediaItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Launch,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(12.dp)
                    .size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialMediaCardPreview() {
    val socialMediaSample = SocialMedia(
        googleMaps = "https://maps.app.goo.gl/M5HfeDqxw6F8ZJRa6",
        whatsApp = "6285659988939",
        instagram = "tripkeun",
        tiktok = "tripkeun",
        youtube = "tripkeun"
    )
    AppTheme {
        SocialMediaCard(
            socialMedia = socialMediaSample,
            onSocialMediaClick = {}
        )
    }
}