@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs.profile.content

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.presentation.ui.theme.montserratAlternatesFontFamily
import java.io.File

@Composable
fun ProfileUI(
    profile: Profile,
    imagePath: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            val imageModel = remember(imagePath) {
                imagePath?.let {
                    val file = File(it)
                    if (file.exists()) {
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
                        onError = {
                            Log.e(
                                "DocsContent",
                                "Image load failed: $imageModel"
                            )
                        },
                        onSuccess = {
                            Log.d(
                                "DocsContent",
                                "Image loaded successfully: $imageModel"
                            )
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
        Column {
            Text(
                text = profile.nameShort,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = montserratAlternatesFontFamily,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = profile.tagline,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}