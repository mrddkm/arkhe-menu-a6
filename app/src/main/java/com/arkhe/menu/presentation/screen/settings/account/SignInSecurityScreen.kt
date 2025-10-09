package com.arkhe.menu.presentation.screen.settings.account

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.presentation.screen.settings.account.components.AccountEditItem
import com.arkhe.menu.presentation.screen.settings.account.components.AccountToggleItem
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import compose.icons.evaicons.outline.Info
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun SignInSecurityScreen(
    onBackClick: () -> Unit,
    user: User
) {
    val handleBackNavigation: () -> Unit = {
        onBackClick()
    }

    Scaffold { paddingValues ->
        SignInSecurityContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = handleBackNavigation,
            user = user
        )
    }
}

@Composable
fun SignInSecurityContent(
    modifier: Modifier = Modifier,
    user: User,
    onHandleBackNavigation: () -> Unit = { },
    langViewModel: LanguageViewModel = koinViewModel()
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHandleBackNavigation) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = EvaIcons.Outline.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = langViewModel.getLocalized(Lang.SIGN_IN_AND_SECURITY),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Settings and recommendations to keep your account secure",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(8.dp))
        }
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                AccountEditItem(
                    label = "UserID",
                    value = user.userId,
                    info = "This your account primary",
                    onClick = {},
                    showIcon = false
                )
                AccountEditItem(
                    label = "Email",
                    value = user.mail,
                    onClick = {}
                )
                AccountEditItem(
                    label = "Phone",
                    value = user.phone,
                    onClick = {}
                )
                AccountEditItem(
                    label = "NickName",
                    value = user.nickName,
                    info = "You can edit in Personal Info",
                    onClick = {},
                    showIcon = false,
                    showDivider = false
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 8.dp, bottom = 0.dp, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "This is your account to sign-in to the application.",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                AccountEditItem(
                    value = "Password",
                    info = "Last changed Jan 10, 2025",
                    onClick = {}
                )
                AccountEditItem(
                    value = "PIN Lock",
                    info = "Last changed Jan 10, 2025",
                    onClick = {}
                )
                AccountToggleItem(
                    value = "Biometric",
                    showDivider = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInSecurityScreenPreview() {
    val previewContext = LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule,
                previewModule
            )
        }
    ) {
        ArkheTheme {
            SignInSecurityScreen(
                onBackClick = {},
                user = sampleUser
            )
        }
    }
}