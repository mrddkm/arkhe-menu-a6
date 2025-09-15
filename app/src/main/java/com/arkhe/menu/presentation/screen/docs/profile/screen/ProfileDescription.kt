@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.utils.DateUtils.formatBirthDate
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Globe
import java.io.File

@Composable
fun ProfileDescription(profile: Profile) {
    var showEnglish by remember { mutableStateOf(false) }
    val imagePath = profile.localImagePath ?: profile.logo

    Log.d("ProfileDescription", "imagePath: $imagePath")

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
                val imageModel = remember(imagePath) {
                    imagePath.let {
                        val file = File(it)
                        if (file.exists()) {
                            Log.d(
                                "ProfileDescription",
                                "📂 imagePath=$imagePath size=${file.length()} bytes"
                            )
                            Uri.fromFile(file)
                        } else null
                    }
                }
                when {
                    imageModel != null -> {
                        AsyncImage(
                            model = imageModel,
                            contentDescription = "Profile Logo",
                            modifier = Modifier.size(96.dp),
                            placeholder = painterResource(R.drawable.image_outline),
                            error = painterResource(R.drawable.alert_triangle_outline),
                            onError = { error ->
                                Log.e(
                                    "ProfileDescription",
                                    "❌ AsyncImage error: ${error.result.throwable.message}"
                                )
                            },
                            onSuccess = {
                                Log.d("ProfileDescription", "✅ Image loaded successfully")
                            }
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(R.drawable.image_outline),
                            contentDescription = "Default Logo",
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }
            }

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
                    IconButton(
                        onClick = { showEnglish = !showEnglish }
                    ) {
                        if (showEnglish) {
                            Icon(
                                imageVector = EvaIcons.Outline.Globe,
                                contentDescription = "Toggle Language English",
                                modifier = Modifier.size(24.dp),
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_id_indonesia),
                                contentDescription = "Toggle Language Indonesia",
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(0.5.dp, Color.LightGray, shape = CircleShape),
                            )
                        }
                    }
                }
            }

            val birthDateFormatted = remember(profile.birthDate) {
                formatBirthDate(profile.birthDate)
            }

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