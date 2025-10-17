package com.arkhe.menu.presentation.screen.settings.privacy

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.R
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.ui.components.LanguageIconEn
import com.arkhe.menu.presentation.ui.components.LanguageIconId
import com.arkhe.menu.presentation.ui.theme.sourceCodeProFontFamily
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showEnglish by remember { mutableStateOf(true) }
    var markdownText by remember { mutableStateOf("") }

    LaunchedEffect(showEnglish) {
        markdownText = loadMarkdownFromAssets(context, showEnglish)
    }

    Scaffold { paddingValues ->
        PrivacyContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = onBackClick,
            showEnglish = showEnglish,
            onLanguageToggle = { showEnglish = !showEnglish },
            markdownText = markdownText,
            context = context
        )
    }
}

@Suppress("DEPRECATION")
@Composable
fun PrivacyContent(
    modifier: Modifier = Modifier,
    onHandleBackNavigation: () -> Unit = { },
    showEnglish: Boolean,
    onLanguageToggle: () -> Unit,
    markdownText: String,
    context: Context
) {
    val textInId = getStringForLocale(context, R.string.privacy_policy, Locale("id"))
    val textInEn = getStringForLocale(context, R.string.privacy_policy, Locale("en"))

    val markdownColors = DefaultMarkdownColors(
        text = Color.Gray,
        codeBackground = Color.Gray,
        inlineCodeBackground = Color.Gray,
        dividerColor = Color.Gray,
        tableBackground = Color.Gray
    )

    val markdownTypography = DefaultMarkdownTypography(
        h1 = TextStyle(fontFamily = sourceCodeProFontFamily),
        h2 = TextStyle(fontFamily = sourceCodeProFontFamily),
        h3 = TextStyle(fontFamily = sourceCodeProFontFamily),
        h4 = TextStyle(fontFamily = sourceCodeProFontFamily),
        h5 = TextStyle(fontFamily = sourceCodeProFontFamily),
        h6 = TextStyle(fontFamily = sourceCodeProFontFamily),
        text = TextStyle(fontFamily = sourceCodeProFontFamily),
        code = TextStyle(fontFamily = sourceCodeProFontFamily),
        inlineCode = TextStyle(fontFamily = sourceCodeProFontFamily),
        quote = TextStyle(fontFamily = sourceCodeProFontFamily),
        paragraph = TextStyle(fontFamily = sourceCodeProFontFamily),
        ordered = TextStyle(fontFamily = sourceCodeProFontFamily),
        bullet = TextStyle(fontFamily = sourceCodeProFontFamily),
        list = TextStyle(fontFamily = sourceCodeProFontFamily),
        textLink = TextLinkStyles(),
        table = TextStyle(fontFamily = sourceCodeProFontFamily)
    )

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
            IconButton(onClick = onLanguageToggle) {
                if (showEnglish) {
                    LanguageIconEn()
                } else {
                    LanguageIconId()
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
                text = textInEn,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = textInId,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 0.dp, bottom = 0.dp, end = 8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Markdown(
                content = markdownText,
                colors = markdownColors,
                typography = markdownTypography
            )
        }
    }
}

private fun loadMarkdownFromAssets(context: Context, showEnglish: Boolean): String {
    val fileName = if (showEnglish) "privacy_policy_en.md" else "privacy_policy_id.md"
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

fun getStringForLocale(context: Context, @StringRes id: Int, locale: Locale): String {
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config).resources.getString(id)
}