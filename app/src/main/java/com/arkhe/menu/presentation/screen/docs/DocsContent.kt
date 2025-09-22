@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.screen.docs

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkhe.menu.R
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.presentation.components.EmptyUI
import com.arkhe.menu.presentation.components.ErrorUI
import com.arkhe.menu.presentation.components.HeaderContent
import com.arkhe.menu.presentation.components.HeaderSection
import com.arkhe.menu.presentation.components.LoadingIndicatorSpinner
import com.arkhe.menu.presentation.screen.docs.categories.content.CategoriesNonScrollableUI
import com.arkhe.menu.presentation.screen.docs.organization.ext.Organization
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilDetailBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.PersonilListBottomSheet
import com.arkhe.menu.presentation.screen.docs.organization.ext.sampleOrganizations
import com.arkhe.menu.presentation.screen.docs.product.content.ProductUI
import com.arkhe.menu.presentation.screen.docs.profile.content.ProfileUI
import com.arkhe.menu.presentation.ui.theme.AppTheme
import com.arkhe.menu.presentation.viewmodel.CategoryViewModel
import com.arkhe.menu.presentation.viewmodel.ProductViewModel
import com.arkhe.menu.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
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

    val profileState by profileViewModel.profilesState.collectAsState()
    val categoriesState by categoryViewModel.categoriesState.collectAsState()
    val productsState by productViewModel.productsState.collectAsState()

    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            try {
                profileViewModel.ensureDataLoaded()
                categoryViewModel.ensureDataLoaded()
                productViewModel.ensureDataLoaded()

                delay(300)
                isInitialized = true
                Log.d("DocsContent", "✅ Offline-first initialization completed")
            } catch (e: Exception) {
                Log.e("DocsContent", "❌ Initialization error: ${e.message}", e)
            }
        }
    }

    Column {
        HeaderContent(
            title = stringResource(R.string.docs)
        )

        /*Profile Content*/
        Card(
            modifier = Modifier
                .clickable { onNavigateToProfile() }
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (profileState) {
                    is SafeApiResult.Loading -> {
                        LoadingIndicatorSpinner(
                            message = stringResource(R.string.profile)
                        )
                    }

                    is SafeApiResult.Success<*> -> {
                        val profiles = (profileState as SafeApiResult.Success<List<Profile>>).data
                        if (profiles.isNotEmpty()) {
                            ProfileUI(
                                profile = profiles.first(),
                                imagePath = profiles.first().localImagePath
                            )
                        } else {
                            EmptyUI(
                                message = stringResource(R.string.profile),
                                onLoad = { profileViewModel.refreshProfiles() }
                            )
                        }
                    }

                    is SafeApiResult.Error -> {
                        ErrorUI(
                            message = stringResource(R.string.profile),
                            exception = (profileState as SafeApiResult.Error).exception as Exception,
                            onRetry = { profileViewModel.refreshProfiles() }
                        )
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            /*Organization Section
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
            */

            /*Customer Section
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
             */

            /*Categories Section*/
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.categories),
                        onHeaderClick = { onNavigateToCategories() }
                    )

                    when (categoriesState) {
                        is SafeApiResult.Loading -> {
                            LoadingIndicatorSpinner(
                                message = stringResource(R.string.profile)
                            )
                        }

                        is SafeApiResult.Success -> {
                            val categories =
                                (categoriesState as SafeApiResult.Success<List<Category>>).data
                            if (categories.isNotEmpty()) {
                                CategoriesNonScrollableUI(categoriesList = categories)
                            } else {
                                EmptyUI(
                                    message = stringResource(R.string.categories),
                                    onLoad = { categoryViewModel.refreshCategories() }
                                )
                            }
                        }

                        is SafeApiResult.Error -> {
                            ErrorUI(
                                message = stringResource(R.string.categories),
                                exception = (categoriesState as SafeApiResult.Error).exception as Exception,
                                onRetry = { categoryViewModel.refreshCategories() }
                            )
                        }
                    }
                }
            }

            /*Products Section*/
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, top = 0.dp, bottom = 0.dp, end = 0.dp)
                ) {
                    HeaderSection(
                        title = stringResource(R.string.products),
                        paddingStart = 16.dp,
                        onHeaderClick = {
                            onNavigateToProducts()
                        }
                    )
                    when (productsState) {
                        is SafeApiResult.Loading -> {
                            LoadingIndicatorSpinner(
                                message = stringResource(R.string.profile)
                            )
                        }

                        is SafeApiResult.Success -> {
                            val products =
                                (productsState as SafeApiResult.Success<List<Product>>).data

                            if (products.isNotEmpty()) {
                                ProductUI(
                                    productList = products
                                )
                            } else {
                                EmptyUI(
                                    message = stringResource(R.string.products),
                                    onLoad = { productViewModel.refreshProducts() }
                                )
                            }
                        }

                        is SafeApiResult.Error -> {
                            ErrorUI(
                                message = stringResource(R.string.products),
                                exception = (productsState as SafeApiResult.Error).exception as Exception,
                                onRetry = { productViewModel.refreshProducts() }
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

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun DocsContentPreview() {
    val previewContext = androidx.compose.ui.platform.LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule
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