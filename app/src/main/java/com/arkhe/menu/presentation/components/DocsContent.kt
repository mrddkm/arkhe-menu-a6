@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DocsContent(onNavigateToContent: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Docs",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Main Content: Profile Tripkeun
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { onNavigateToContent("Profile Tripkeun") },
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "Profile Tripkeun",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Profile Tripkeun",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Informasi lengkap tentang perusahaan",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Secondary Contents in Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DocsCard(
                    title = "Profile Team",
                    description = "Tim Tripkeun",
                    icon = Icons.Default.Group,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToContent("Profile Team Tripkeun") }
                )
                DocsCard(
                    title = "Top 10 Sobatkeun",
                    description = "Member terbaik",
                    icon = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToContent("Top 10 Sobatkeun") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DocsCard(
                    title = "Product Category",
                    description = "Kategori produk",
                    icon = Icons.Default.Category,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToContent("Product Category") }
                )
                DocsCard(
                    title = "Products",
                    description = "Daftar produk",
                    icon = Icons.Default.Inventory,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToContent("Products") }
                )
            }
        }
    }
}

@Composable
fun DocsCard(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DocsContentPreview() {
    DocsContent(onNavigateToContent = {})
}