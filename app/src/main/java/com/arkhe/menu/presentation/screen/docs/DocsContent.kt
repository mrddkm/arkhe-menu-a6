@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.presentation.screen.docs.category.ext.CategorySection
import com.arkhe.menu.presentation.screen.docs.category.ext.sampleCategory
import com.arkhe.menu.presentation.screen.docs.ext.HeaderSection
import com.arkhe.menu.presentation.screen.docs.product.ext.ProductSection
import com.arkhe.menu.presentation.screen.docs.product.ext.sampleProduct
import com.arkhe.menu.presentation.screen.docs.sobatkeun.ext.SobatkeunSection
import com.arkhe.menu.presentation.screen.docs.sobatkeun.ext.sampleSobatkeun
import com.arkhe.menu.presentation.screen.docs.team.ext.Personil
import com.arkhe.menu.presentation.screen.docs.team.ext.PersonilDetailBottomSheet
import com.arkhe.menu.presentation.screen.docs.team.ext.PersonilListBottomSheet
import com.arkhe.menu.presentation.screen.docs.team.ext.PersonilSection
import com.arkhe.menu.presentation.screen.docs.team.ext.samplePersonil
import com.arkhe.menu.presentation.theme.AppTheme

@Composable
fun DocsContent(onNavigateToContent: (String) -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedPersonil by remember { mutableStateOf<Personil?>(null) }
    var showPersonilList by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Docs",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        /*Main Content*/
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onNavigateToContent("Profile Tripkeun") },
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
        }

        /*Secondary Contents*/
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    HeaderSection(
                        title = "Team",
                        onHeaderClick = { showPersonilList = true }
                    )
                    PersonilSection(
                        personilList = samplePersonil,
                        onPersonilClick = { personil ->
                            selectedPersonil = personil
                            showBottomSheet = true
                        }
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    HeaderSection(
                        title = "Sobatkeun",
                        onHeaderClick = { showPersonilList = true }
                    )
                    SobatkeunSection(
                        sobatkeunList = sampleSobatkeun,
                        onSobatkeunClick = { /* Handle Sobatkeun click if needed */ }
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    HeaderSection(
                        title = "Categories",
                        onHeaderClick = { showPersonilList = true }
                    )
                    CategorySection(
                        categoryList = sampleCategory,
                        onCategoryClick = { /* Handle Category click if needed */ }
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    HeaderSection(
                        title = "Products",
                        onHeaderClick = { showPersonilList = true }
                    )
                    ProductSection(
                        productList = sampleProduct,
                        onProductClick = { /* Handle Product click if needed */ }
                    )
                }
            }
        }
    }

    if (showBottomSheet && selectedPersonil != null) {
        PersonilDetailBottomSheet(
            personil = selectedPersonil!!,
            onDismiss = {
                showBottomSheet = false
                selectedPersonil = null
            }
        )
    }

    if (showPersonilList) {
        PersonilListBottomSheet(
            personilList = samplePersonil,
            onPersonilClick = { personil ->
                selectedPersonil = personil
                showPersonilList = false
                showBottomSheet = true
            },
            onDismiss = { showPersonilList = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DocsContentPreview() {
    AppTheme(darkTheme = false) {
        DocsContent(onNavigateToContent = {})
    }
}