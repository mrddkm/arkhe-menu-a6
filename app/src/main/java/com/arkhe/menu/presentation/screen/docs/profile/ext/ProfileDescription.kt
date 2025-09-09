@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile.ext

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
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileDescription(profile: Profile) {
    var showEnglish by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var isLoadingImage by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(profile.nameShort) {
        if (profile.nameShort.isNotBlank()) {
            isLoadingImage = true
            imagePath = try {
                val manager = ImageStorageManager(context)
                manager.getLocalImagePath(profile.nameShort) ?: profile.logo
            } catch (_: Exception) {
                null
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
                            CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        }
                    }

                    imagePath != null -> {
                        AsyncImage(
                            model = imagePath,
                            contentDescription = "Profile Logo",
                            modifier = Modifier.size(96.dp),
                            placeholder = painterResource(R.drawable.bitrise),
                            error = painterResource(R.drawable.searxng),
                            onError = {
                                // If image fails to load, we could set imagePath to null
                                // to show default image instead
                            }
                        )
                    }

                    else -> {
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

                if (profile.information.indonesian.isNotBlank() && profile.information.english.isNotBlank()) {
                    TextButton(
                        onClick = { showEnglish = !showEnglish }
                    ) {
                        Text(if (showEnglish) "ID" else "EN")
                    }
                }
            }

            // Birth Date Formatting
            val birthDateFormatted = remember(profile.birthDate) {
                try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    val date = inputFormat.parse(profile.birthDate)
                    date?.let { outputFormat.format(it) } ?: profile.birthDate
                } catch (_: Exception) {
                    try {
                        val altFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        val date = altFormat.parse(profile.birthDate)
                        date?.let { outputFormat.format(it) } ?: profile.birthDate
                    } catch (_: Exception) {
                        profile.birthDate
                    }
                }
            }

            // Action Information
            val actionText = remember(showEnglish, profile.actionInfo) {
                val action = when {
                    showEnglish && profile.actionInfo.information.english.isNotBlank() ->
                        profile.actionInfo.information.english
                    profile.actionInfo.information.indonesian.isNotBlank() ->
                        profile.actionInfo.information.indonesian
                    profile.actionInfo.information.english.isNotBlank() ->
                        profile.actionInfo.information.english
                    else -> "No information available"
                }

                action.replace("{profileNameShort}", "\"${profile.nameShort}\"")
                    .replace("{birthDate}", birthDateFormatted)
            }

            Text(
                text = actionText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Profile Information
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
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.2)
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