@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.components.common.LoadingIndicator
import com.arkhe.menu.presentation.screen.docs.profile.ext.EmptyProfileState
import com.arkhe.menu.presentation.screen.docs.profile.ext.ErrorCard
import com.arkhe.menu.presentation.screen.docs.profile.ext.ProfileDescription
import com.arkhe.menu.presentation.screen.docs.profile.ext.ProfileTagLine
import com.arkhe.menu.presentation.screen.docs.profile.ext.SocialMediaCard
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            // You can show snackbar or dialog here
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
            if (uiState.isRefreshing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Refreshing...",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (uiState.isLoading) {
                LoadingIndicator()
            } else {
                val profile = viewModel.getProfile()

                if (profile != null) {
                    ProfileContent(
                        profile = profile,
                        onSocialMediaClick = { url ->
                            try {
                                uriHandler.openUri(url)
                            } catch (_: Exception) {
                                // Handle URL opening error silently
                            }
                        }
                    )
                } else {
                    EmptyProfileState(
                        onRetryClick = { viewModel.loadProfiles(true) }
                    )
                }
            }

            uiState.error?.let { error ->
                ErrorCard(
                    error = error,
                    onRetryClick = { viewModel.loadProfiles(true) },
                    onDismissClick = { viewModel.clearError() }
                )
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
            information = ProfileInformation(
                indonesian = "Tripkeun Indonesia adalah gerbang menuju petualangan tanpa batas.",
                english = "Tripkeun Indonesia is your gateway to limitless adventures."
            ),
            actionInfo = ActionInfo(
                action = "profile",
                information = ProfileInformation(
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