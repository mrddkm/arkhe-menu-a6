package com.arkhe.menu.presentation.screen.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WizardBottomSheetExample() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // State untuk melacak halaman wizard saat ini
    var currentPage by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            showBottomSheet = true
            currentPage = 1 // Reset ke halaman 1
        }) {
            Text("Buka Wizard")
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                currentPage = 1
            },
            sheetState = sheetState
        ) {
            WizardContent(
                currentPage = currentPage,
                onNext = { currentPage++ },
                onPrevious = { currentPage-- },
                onFinish = {
                    showBottomSheet = false
                    currentPage = 1
                }
            )
        }
    }
}

@Composable
fun WizardContent(
    currentPage: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        // Progress Indicator
        LinearProgressIndicator(
            progress = { currentPage / 4f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Langkah $currentPage dari 4",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Konten berdasarkan halaman
        when (currentPage) {
            1 -> Page1()
            2 -> Page2()
            3 -> Page3()
            4 -> Page4()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol navigasi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tombol Kembali
            if (currentPage > 1) {
                OutlinedButton(onClick = onPrevious) {
                    Text("Kembali")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            // Tombol Lanjut/Selesai
            Button(
                onClick = {
                    if (currentPage < 4) {
                        onNext()
                    } else {
                        onFinish()
                    }
                }
            ) {
                Text(if (currentPage < 4) "Lanjut" else "Selesai")
            }
        }
    }
}

@Composable
fun Page1() {
    Column {
        Text(
            text = "Halaman 1: Informasi Dasar",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Page2() {
    Column {
        Text(
            text = "Halaman 2: Preferensi",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text("Pilih kategori favorit Anda:")
        Spacer(modifier = Modifier.height(12.dp))

        listOf("Teknologi", "Olahraga", "Musik", "Makanan").forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = false, onCheckedChange = {})
                Text(item)
            }
        }
    }
}

@Composable
fun Page3() {
    Column {
        Text(
            text = "Halaman 3: Pengaturan",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notifikasi")
            Switch(checked = true, onCheckedChange = {})
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mode Gelap")
            Switch(checked = false, onCheckedChange = {})
        }
    }
}

@Composable
fun Page4() {
    Column {
        Text(
            text = "Halaman 4: Ringkasan",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✓ Informasi dasar telah diisi",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Preferensi telah dipilih",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Pengaturan telah dikonfigurasi",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}