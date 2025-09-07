@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.presentation.screen.docs.categories.ext.CategoriesSectionContent
import com.arkhe.menu.presentation.screen.docs.components.HeaderSection
import com.arkhe.menu.presentation.screen.docs.customer.ext.CustomerSection
import com.arkhe.menu.presentation.screen.docs.customer.ext.sampleCustomers
import com.arkhe.menu.presentation.screen.docs.organization.ext.Organization
import com.arkhe.menu.presentation.screen.docs.organization.ext.OrganizationSection
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilDetailBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilListBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.sampleOrganizations
import com.arkhe.menu.presentation.screen.docs.product.ext.ProductSectionContent
import com.arkhe.menu.presentation.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DocsContent(
    onNavigateToProfile: () -> Unit,
    onNavigateToOrganization: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    profileViewModel: ProfileViewModel = koinViewModel(),
    categoryViewModel: CategoryViewModel = koinViewModel(),
    productViewModel: ProductViewModel = koinViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOrganization by remember { mutableStateOf<Organization?>(null) }
    var showPersonilList by remember { mutableStateOf(false) }

    val profileState by profileViewModel.uiState.collectAsState()
    val categoriesState by categoryViewModel.categoriesState.collectAsState()
    val productsState by productViewModel.productsState.collectAsState()

    LaunchedEffect(Unit) {
        categoryViewModel.ensureDataLoaded()
//        productViewModel.ensureDataLoaded()
    }

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
                    Image(
                        painter = painterResource(R.drawable.tripkeun_official),
                        contentDescription = null,
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

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            /*Organization Section*/
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

            /*Customer Section (unchanged)*/
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

            /*Categories Section (with API)*/
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
                        is SafeApiResult.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is SafeApiResult.Success -> {
                            CategoriesSectionContent(
                                categoriesList = (categoriesState as SafeApiResult.Success<List<Category>>).data
                            )
                        }

                        is SafeApiResult.Error -> {
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

            /*Products Section (with API - productCategoryId = "ALL")*/
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
                    when (productsState) {
                        is SafeApiResult.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is SafeApiResult.Success -> {
                            ProductSectionContent(
                                productList = (productsState as SafeApiResult.Success<List<Product>>).data
                            )
                        }

                        is SafeApiResult.Error -> {
                            Text(
                                text = "Failed to load products",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
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
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                appModule, dataModule, domainModule
            )
        }
    ) {
        AppTheme {
            DocsContent(
                onNavigateToProfile = {}
            )
        }
    }
}