@file:Suppress("SpellCheckingInspection")
@file:OptIn(ExperimentalMaterial3Api::class)

package com.arkhe.menu.presentation.screen.docs.team.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun PersonilSection(
    personilList: List<Personil>,
    onPersonilClick: (Personil) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(personilList) { personil ->
                PersonilCard(
                    personil = personil,
                    onClick = { onPersonilClick(personil) }
                )
            }
        }
    }
}

@Composable
fun PersonilCard(
    personil: Personil,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = personil.fotoRes),
                    contentDescription = personil.nama,
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFF2E7D32)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = personil.nama,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = personil.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Left,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

@Composable
fun PersonilDetailBottomSheet(
    personil: Personil,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header dengan foto dan nama
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = personil.fotoRes),
                            contentDescription = personil.nama,
                            modifier = Modifier.size(50.dp),
                            tint = Color(0xFF2E7D32)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = personil.nama,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = personil.posisi,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF757575)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Informasi detail
            DetailInfoSection(label = "Pengalaman", value = personil.pengalaman)
            DetailInfoSection(label = "Email", value = personil.email)
            DetailInfoSection(label = "Telepon", value = personil.telepon)

            Spacer(modifier = Modifier.height(16.dp))

            // Bio
            Text(
                text = "Tentang",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = personil.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF424242),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PersonilListBottomSheet(
    personilList: List<Personil>,
    onPersonilClick: (Personil) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Anggota Tim Tripkeun",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1B5E20)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF757575)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List personil
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                personilList.forEach { personil ->
                    PersonilListItem(
                        personil = personil,
                        onClick = { onPersonilClick(personil) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PersonilListItem(
    personil: Personil,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = personil.fotoRes),
                    contentDescription = personil.nama,
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = personil.nama,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = personil.posisi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

@Composable
fun DetailInfoSection(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF1B5E20)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF424242)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PersonilCardPreview() {
    AppTheme {
        PersonilSection(
            personilList = samplePersonil,
            onPersonilClick = { }
        )
    }
}