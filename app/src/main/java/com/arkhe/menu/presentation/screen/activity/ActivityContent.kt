@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun ActivityContent(
    userRole: UserRole,
    onNavigateToContent: (String) -> Unit
) {
    Column {
        Text(
            text = "Activity",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Current Role: ${userRole.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Role-based access control menus",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        when (userRole) {
            UserRole.FAGA -> FAGAContent(onNavigateToContent)
            UserRole.SUPERUSER -> SuperuserContent(onNavigateToContent)
            UserRole.MCC -> MCCContent(onNavigateToContent)
            UserRole.BROD -> BRODContent(onNavigateToContent)
            UserRole.PRESDIR -> PresdirContent(onNavigateToContent)
            UserRole.UNSPECIFIED -> UnspecifiedContent(onNavigateToContent)
        }
    }
}

@Composable
fun FAGAContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "FAGA Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "Create Receipt",
            description = "Buat kuitansi baru",
            icon = Icons.Default.Receipt,
            onClick = { onNavigateToContent("Create Receipt") }
        )

        ActivityMenuCard(
            title = "View Receipts",
            description = "Lihat semua kuitansi",
            icon = Icons.AutoMirrored.Filled.List,
            onClick = { onNavigateToContent("View Receipts") }
        )

        ActivityMenuCard(
            title = "Financial Reports",
            description = "Laporan keuangan",
            icon = Icons.Default.Assessment,
            onClick = { onNavigateToContent("Financial Reports") }
        )

        ActivityMenuCard(
            title = "Budget Planning",
            description = "Perencanaan anggaran",
            icon = Icons.Default.AccountBalance,
            onClick = { onNavigateToContent("Budget Planning") }
        )
    }
}

@Composable
fun SuperuserContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Administrator Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "User Management",
            description = "Kelola pengguna sistem",
            icon = Icons.Default.SupervisorAccount,
            onClick = { onNavigateToContent("User Management") }
        )

        ActivityMenuCard(
            title = "System Settings",
            description = "Pengaturan sistem",
            icon = Icons.Default.Settings,
            onClick = { onNavigateToContent("System Settings") }
        )

        ActivityMenuCard(
            title = "Database Backup",
            description = "Backup dan restore data",
            icon = Icons.Default.Backup,
            onClick = { onNavigateToContent("Database Backup") }
        )
    }
}

@Composable
fun MCCContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Marketing & Content Creator Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "Content Management",
            description = "Kelola konten marketing",
            icon = Icons.Default.Create,
            onClick = { onNavigateToContent("Content Management") }
        )

        ActivityMenuCard(
            title = "Campaign Analytics",
            description = "Analisis kampanye marketing",
            icon = Icons.Default.Analytics,
            onClick = { onNavigateToContent("Campaign Analytics") }
        )
    }
}

@Composable
fun BRODContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Business R&D Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "Business Analysis",
            description = "Analisis bisnis dan riset",
            icon = Icons.Default.BusinessCenter,
            onClick = { onNavigateToContent("Business Analysis") }
        )

        ActivityMenuCard(
            title = "Operations Dashboard",
            description = "Dashboard operasional",
            icon = Icons.Default.Dashboard,
            onClick = { onNavigateToContent("Operations Dashboard") }
        )
    }
}

@Composable
fun PresdirContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "President Director Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "Executive Dashboard",
            description = "Dashboard eksekutif",
            icon = Icons.Default.Dashboard,
            onClick = { onNavigateToContent("Executive Dashboard") }
        )

        ActivityMenuCard(
            title = "Strategic Planning",
            description = "Perencanaan strategis",
            icon = Icons.Default.Assessment,
            onClick = { onNavigateToContent("Strategic Planning") }
        )
    }
}

@Composable
fun UnspecifiedContent(onNavigateToContent: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Employee Menu",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ActivityMenuCard(
            title = "My Tasks",
            description = "Tugas saya",
            icon = Icons.AutoMirrored.Filled.Assignment,
            onClick = { onNavigateToContent("My Tasks") }
        )

        ActivityMenuCard(
            title = "Time Tracking",
            description = "Pelacakan waktu kerja",
            icon = Icons.Default.AccessTime,
            onClick = { onNavigateToContent("Time Tracking") }
        )
    }
}

@Composable
fun ActivityMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityContentPreview() {
    AppTheme {
        ActivityContent(
            userRole = UserRole.FAGA,
            onNavigateToContent = {}
        )
    }
}