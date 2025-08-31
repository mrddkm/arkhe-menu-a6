@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.presentation.screen.docs.categories.ext.CategoriesSection
import com.arkhe.menu.presentation.screen.docs.categories.ext.CategoryDetailBottomSheet
import com.arkhe.menu.presentation.screen.docs.components.HeaderSection
import com.arkhe.menu.presentation.screen.docs.customer.ext.CustomerSection
import com.arkhe.menu.presentation.screen.docs.customer.ext.sampleCustomers
import com.arkhe.menu.presentation.screen.docs.organization.ext.Organization
import com.arkhe.menu.presentation.screen.docs.organization.ext.OrganizationSection
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilDetailBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilListBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.sampleOrganizations
import com.arkhe.menu.presentation.screen.docs.product.ext.ProductSection
import com.arkhe.menu.presentation.screen.docs.product.ext.sampleProduct
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DocsContent(
    onNavigateToProfile: () -> Unit,
    onNavigateToOrganization: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    categoryViewModel: CategoryViewModel = koinViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOrganization by remember { mutableStateOf<Organization?>(null) }
    var showPersonilList by remember { mutableStateOf(false) }

    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val categoriesState by categoryViewModel.categoriesState.collectAsState()

    Column {
        Text(
            text = stringResource(R.string.docs),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
        )

        /*Main Content*/
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 8.dp, bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        onNavigateToProfile()
                    },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.client_long_name),
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
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.organization),
                        onHeaderClick = {
                            onNavigateToOrganization()
                        }
                    )
                    OrganizationSection(
                        organizationList = sampleOrganizations,
                        onOrganizationClick = { organization ->
                            selectedOrganization = organization
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
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.customer),
                        onHeaderClick = {
                            onNavigateToCustomer()
                        }
                    )
                    CustomerSection(
                        customerList = sampleCustomers,
                        onCustomerClick = { /* Handle Sobatkeun click if needed */ }
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
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.categories),
                        onHeaderClick = {
                            onNavigateToCategories()
                        }
                    )
                    when (categoriesState) {
                        is ApiResult.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is ApiResult.Success -> {
                            CategoriesSection(
                                categoriesList = (categoriesState as ApiResult.Success<List<Category>>).data,
                                onCategoriesClick = { category ->
                                    selectedCategory = category
                                    showCategoryBottomSheet = true
                                }
                            )
                        }

                        is ApiResult.Error -> {
                            // Fallback to sample data or show error
                            Text(
                                text = "Failed to load categories",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
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
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.products),
                        onHeaderClick = {
                            onNavigateToProducts()
                        }
                    )
                    ProductSection(
                        productList = sampleProduct,
                        onProductClick = { /* Handle Product click if needed */ }
                    )
                }
            }
        }
    }

    /*Category Detail BottomSheet*/
    if (showCategoryBottomSheet && selectedCategory != null) {
        CategoryDetailBottomSheet(
            category = selectedCategory!!,
            onDismiss = {
                showCategoryBottomSheet = false
                selectedCategory = null
            }
        )
    }

    if (showBottomSheet && selectedOrganization != null) {
        PersonilDetailBottomSheet(
            organization = selectedOrganization!!,
            onDismiss = {
                showBottomSheet = false
                selectedOrganization = null
            }
        )
    }

    if (showPersonilList) {
        PersonilListBottomSheet(
            organizationList = sampleOrganizations,
            onPersonilClick = { personil ->
                selectedOrganization = personil
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
        DocsContent(
            onNavigateToProfile = {}
        )
    }
}