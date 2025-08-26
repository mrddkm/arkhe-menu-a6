@file:Suppress("SpellCheckingInspection")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.arkhe.menu.presentation.screen.docs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun HeaderSection(
    title: String,
    onHeaderClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onHeaderClick) {
                println("HeaderSection clicked: $title")
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 2.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    AppTheme {
        HeaderSection(
            title = "Header Title",
            onHeaderClick = {}
        )
    }
}