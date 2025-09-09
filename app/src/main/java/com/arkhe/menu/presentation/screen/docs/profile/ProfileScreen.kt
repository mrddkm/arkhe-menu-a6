@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia
import com.arkhe.menu.presentation.screen.docs.profile.ext.ProfileDescription
import com.arkhe.menu.presentation.screen.docs.profile.ext.ProfileTagLine
import com.arkhe.menu.presentation.screen.docs.profile.ext.SocialMediaCard
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

    LaunchedEffect(Unit) {
        profileViewModel.ensureDataLoaded()
    }

    LaunchedEffect(profileState) {
        if (profileState !is SafeApiResult.Loading) {
            isRefreshing = false
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
                is SafeApiResult.Error -> TODO()
                is SafeApiResult.Success<*> -> {
                    val profile =
                        (profileState as SafeApiResult.Success<List<Profile>>).data.first()
                    ProfileContent(
                        profile = profile,
                        onSocialMediaClick = { url ->
                            try {
                                uriHandler.openUri(url)
                            } catch (_: Exception) {
                            }
                        }
                    )
                }

                is SafeApiResult.Loading -> TODO()
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