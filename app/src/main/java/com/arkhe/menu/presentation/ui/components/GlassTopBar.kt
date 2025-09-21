package com.arkhe.menu.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ArrowIosBack
import kotlin.math.min

@Composable
fun GlassTopBar(
    title: String,
    listState: LazyListState,
    isInMainContent: Boolean,
    previousLabel: String? = null,
    onBackClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
) {
    val scrollOffset by remember {
        derivedStateOf { min(1f, listState.firstVisibleItemScrollOffset / 200f) }
    }

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = MaterialTheme.shapes.large.copy(all = CornerSize(0.dp)),
        blurRadius = 30.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 👈 LEFT
            if (!isInMainContent && onBackClick != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = EvaIcons.Outline.ArrowIosBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    previousLabel?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) // placeholder
            }

            // 👆 CENTER TITLE
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = if (isInMainContent) Modifier.alpha(scrollOffset) else Modifier
            )

            // 👉 RIGHT
            if (!isInMainContent && onProfileClick != null) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}