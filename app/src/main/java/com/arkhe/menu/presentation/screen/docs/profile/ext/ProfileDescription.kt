@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile.ext

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.data.local.storage.ImageStorageManager
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.utils.DateUtils.formatBirthDate

@Composable
fun ProfileDescription(profile: Profile) {
    var showEnglish by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var isLoadingImage by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(profile.nameShort) {
        if (profile.nameShort.isNotBlank()) {
            try {
                isLoadingImage = true
                Log.d("ProfileDescription", "🖼️ Loading image for: ${profile.nameShort}")

                val manager = ImageStorageManager(context)
                val localPath = manager.getLocalImagePath(profile.nameShort)

                imagePath = localPath ?: profile.logo

                Log.d("ProfileDescription", "✅ Image path resolved: $imagePath")
            } catch (e: Exception) {
                Log.e("ProfileDescription", "❌ Error loading image: ${e.message}")
                imagePath = profile.logo
            } finally {
                isLoadingImage = false
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Image Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    isLoadingImage -> {
                        Box(
                            modifier = Modifier.size(96.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    }

                    !imagePath.isNullOrEmpty() -> {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = "Profile Logo",
                            modifier = Modifier.size(96.dp),
                            placeholder = painterResource(R.drawable.bitrise),
                            error = painterResource(R.drawable.searxng),
                            onError = { error ->
                                Log.e(
                                    "ProfileDescription",
                                    "❌ AsyncImage error: ${error.result.throwable.message}"
                                )
                                // Could set imagePath to null here to show default image
                            },
                            onSuccess = {
                                Log.d("ProfileDescription", "✅ Image loaded successfully")
                            }
                        )
                    }

                    else -> {
                        Log.d("ProfileDescription", "📁 Using default image")
                        Image(
                            painter = painterResource(R.drawable.devbox),
                            contentDescription = "Default Logo",
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }
            }

            // Profile Title and Language Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile.nameLong,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                // Show language toggle only if both languages have content
                if (profile.information.indonesian.isNotBlank() && profile.information.english.isNotBlank()) {
                    TextButton(
                        onClick = { showEnglish = !showEnglish }
                    ) {
                        Text(
                            text = if (showEnglish) "ID" else "EN",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            val birthDateFormatted = remember(profile.birthDate) {
                formatBirthDate(profile.birthDate)
            }

            // Action Information with smart text replacement
            val actionText = remember(showEnglish, profile.actionInfo, birthDateFormatted) {
                val information = when {
                    showEnglish && profile.actionInfo.information.english.isNotBlank() ->
                        profile.actionInfo.information.english

                    profile.actionInfo.information.indonesian.isNotBlank() ->
                        profile.actionInfo.information.indonesian

                    profile.actionInfo.information.english.isNotBlank() ->
                        profile.actionInfo.information.english

                    else -> "No information available"
                }

                information
                    .replace("{profileNameShort}", "\"${profile.nameShort}\"")
                    .replace("{birthDate}", birthDateFormatted)
            }

            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.2)
            )

            // Profile Information with language preference
            val informationText = remember(showEnglish, profile.information) {
                when {
                    showEnglish && profile.information.english.isNotBlank() ->
                        profile.information.english

                    profile.information.indonesian.isNotBlank() ->
                        profile.information.indonesian

                    profile.information.english.isNotBlank() ->
                        profile.information.english

                    else -> "No information available"
                }
            }

            Text(
                text = informationText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.2),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    AppTheme {
        val sampleProfile = Profile(
            nameShort = "tripkeun",
            nameLong = "Tripkeun Indonesia",
            birthDate = "2023-04-20T16:59:59.000Z",
            logo = "https://drive.google.com/file/d/1p_2G8bRhX5KJjZ2N0IGRd5B2Fv3cFMoh/view?usp=sharing",
            socialMedia = SocialMedia(
                googleMaps = "https://maps.app.goo.gl/M5HfeDqxw6F8ZJRa6",
                whatsApp = "6285659988939",
                instagram = "tripkeun",
                tiktok = "tripkeun",
                youtube = "tripkeun"
            ),
            tagline = "More Than a Journey, It's a Connection",
            quotes = "The best journey isn't about distance, but about who walks with you.",
            information = ProfileInformation(
                indonesian = "Tripkeun Indonesia adalah gerbang menuju petualangan tanpa batas.",
                english = "Tripkeun Indonesia is your gateway to limitless adventures."
            ),
            actionInfo = ActionInfo(
                action = "profile",
                information = ProfileInformation(
                    indonesian = "Panggil Kami {profileNameShort} lahir di Bandung pada {birthDate}.",
                    english = "Call us {profileNameShort} born in Bandung on {birthDate}."
                )
            )
        )
        ProfileDescription(
            profile = sampleProfile
        )
    }
}