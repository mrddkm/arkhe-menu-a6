@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.ProfileActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformationLanguage
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.screen.docs.profile.screen.ProfileDescription
import com.arkhe.menu.presentation.screen.docs.profile.screen.ProfileTagLine
import com.arkhe.menu.presentation.screen.docs.profile.screen.SocialMediaCard
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val profileState by profileViewModel.profilesState.collectAsState()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    var isRefreshing by remember { mutableStateOf(false) }
    val lastSuccess = remember { mutableStateOf<List<Profile>?>(null) }

    LaunchedEffect(Unit) {
        profileViewModel.ensureDataLoaded()
    }

    LaunchedEffect(profileState) {
        if (profileState !is SafeApiResult.Loading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(profileState) {
        if (profileState is SafeApiResult.Success) {
            lastSuccess.value = (profileState as SafeApiResult.Success<List<Profile>>).data
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (profileState) {
                is SafeApiResult.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Loading profile...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }

                is SafeApiResult.Error -> {
                    if (!lastSuccess.value.isNullOrEmpty()) {
                        ProfileContent(
                            profile = lastSuccess.value!!.first(),
                            onSocialMediaClick = { url ->
                                try {
                                    uriHandler.openUri(url)
                                } catch (_: Exception) {
                                }
                            }
                        )
                        Text(
                            text = "Gagal sync, menampilkan data lama",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text(
                                    text = "Failed to load profile",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )

                                (profileState as SafeApiResult.Error).exception.message?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        isRefreshing = true
                                        profileViewModel.refreshProfiles()
                                    },
                                    enabled = !isRefreshing
                                ) {
                                    Text(if (isRefreshing) "Retrying..." else "Try Again")
                                }
                            }
                        }
                    }
                }

                is SafeApiResult.Success<*> -> {
                    val profiles = (profileState as SafeApiResult.Success<List<Profile>>).data
                    if (profiles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No profile data available",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )

                                Button(
                                    onClick = {
                                        isRefreshing = true
                                        profileViewModel.refreshProfiles()
                                    },
                                    enabled = !isRefreshing,
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(if (isRefreshing) "Refreshing..." else "Refresh")
                                }
                            }
                        }
                    } else {
                        ProfileContent(
                            profile = profiles.first(),
                            onSocialMediaClick = { url ->
                                try {
                                    uriHandler.openUri(url)
                                } catch (_: Exception) {
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: Profile,
    onSocialMediaClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileDescription(profile = profile)
        ProfileTagLine(profile = profile)
        SocialMediaCard(
            socialMedia = profile.socialMedia,
            onSocialMediaClick = onSocialMediaClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
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
            information = ProfileInformationLanguage(
                indonesian = "Tripkeun Indonesia adalah gerbang menuju petualangan tanpa batas.",
                english = "Tripkeun Indonesia is your gateway to limitless adventures."
            ),
            profileActionInfo = ProfileActionInfo(
                action = "profile",
                information = ProfileInformationLanguage(
                    indonesian = "Perbarui informasi perusahaan Anda untuk tetap terhubung dengan pelanggan.",
                    english = "Update your company information to stay connected with customers."
                )
            )
        )
        ProfileContent(
            profile = sampleProfile,
            onSocialMediaClick = {}
        )
    }
}