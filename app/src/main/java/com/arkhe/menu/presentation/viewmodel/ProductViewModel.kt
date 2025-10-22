package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductByGroup
import com.arkhe.menu.domain.model.ProductStatistics
import com.arkhe.menu.domain.usecase.product.ProductUseCases
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import com.arkhe.menu.utils.Constants.CurrentLanguage.INDONESIAN
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_INITIATION
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_READY
import com.arkhe.menu.utils.Constants.Statistics.STATISTICS_RESEARCH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productUseCases: ProductUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "ProductViewModel"
    }

    private val _productsState =
        MutableStateFlow<SafeApiResult<List<Product>>>(SafeApiResult.Loading)
    val productsState: StateFlow<SafeApiResult<List<Product>>> = _productsState.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    private val _productByGroups = MutableStateFlow<List<ProductByGroup>>(emptyList())
    val productByGroups: StateFlow<List<ProductByGroup>> = _productByGroups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<ProductByGroup?>(null)
    val selectedGroup: StateFlow<ProductByGroup?> = _selectedGroup.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    private val _currentProductCategoryId = MutableStateFlow("ALL")
    val currentProductCategoryId: StateFlow<String> = _currentProductCategoryId.asStateFlow()

    private var isInitialized = false

    private var _persistentSelectedSeriesName: String? = null

    init {
        viewModelScope.launch {
            try {
                productUseCases.getProducts(sessionManager.getTokenForApiCall())
                    .collectLatest { productsResult ->
                        _productsState.value = productsResult

                        if (productsResult is SafeApiResult.Success && productsResult.data.isNotEmpty()) {
                            loadProductGroups()
                        }
                    }
            } catch (e: Exception) {
                _productsState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun loadProducts(
        productCategoryId: String = "ALL",
        forceRefresh: Boolean = false
    ) {
        if (!forceRefresh) {
            return
        }

        viewModelScope.launch {
            try {
                _currentProductCategoryId.value = productCategoryId
                _productsState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()

                val result = productUseCases.syncProducts(sessionToken, productCategoryId)
                when (result) {
                    is SafeApiResult.Loading -> {
                        _productsState.value = result
                    }

                    is SafeApiResult.Success -> {
                        _productsState.value = result
                        loadProductGroups()
                    }

                    is SafeApiResult.Error -> {
                        _productsState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
                _productsState.value = SafeApiResult.Error(e)
            }
        }
    }

    private fun handleTokenError(exception: Throwable, alreadyRetried: Boolean) {
        val errorMessage = exception.message?.lowercase() ?: ""
        val isTokenError = errorMessage.contains("token") ||
                errorMessage.contains("unauthorized") ||
                errorMessage.contains("authentication")

        if (isTokenError && !alreadyRetried) {
            viewModelScope.launch {
                try {
                    val newToken = sessionManager.ensureTokenAvailable()
                    loadProducts(_currentProductCategoryId.value, forceRefresh = true)
                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "❌ Error in token refresh retry: ${e.message}"
                    )
                }
            }
        }
    }

    fun ensureDataLoaded() {
        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    val productsResult = productUseCases.getProducts(
                        sessionManager.getTokenForApiCall()
                    ).firstOrNull()
                    when (productsResult) {
                        is SafeApiResult.Success -> {
                            if (productsResult.data.isEmpty()) {
                                loadProducts(_currentProductCategoryId.value, forceRefresh = true)
                            } else {
                                _productsState.value = productsResult
                                loadProductGroups()
                            }
                        }

                        is SafeApiResult.Error -> {
                            _productsState.value = productsResult
                        }

                        SafeApiResult.Loading -> {
                        }

                        null -> {
                            loadProducts(_currentProductCategoryId.value, forceRefresh = true)
                        }
                    }
                    isInitialized = true
                } catch (e: Exception) {
                    _productsState.value = SafeApiResult.Error(e)
                }
            }
        } else {
            when (_productsState.value) {
                is SafeApiResult.Error -> {
                    loadProducts(_currentProductCategoryId.value, forceRefresh = false)
                }

                is SafeApiResult.Loading -> {
                }

                else -> {
                    if (_productByGroups.value.isEmpty()) {
                        loadProductGroups()
                    }
                }
            }
        }
    }

    private fun loadProductGroups() {
        viewModelScope.launch {
            try {
                val groups = productUseCases.getProductGroups()
                _productByGroups.value = groups

                restoreSelectedGroupIfNeeded()
            } catch (_: Exception) {
                _productByGroups.value = emptyList()
            }
        }
    }

    private fun restoreSelectedGroupIfNeeded() {
        _persistentSelectedSeriesName?.let { seriesName ->
            if (_selectedGroup.value?.seriesName != seriesName) {
                val restoredGroup = _productByGroups.value.find { it.seriesName == seriesName }
                if (restoredGroup != null) {
                    _selectedGroup.value = restoredGroup
                    _filteredProducts.value = restoredGroup.products
                } else {
                    Log.w(TAG, "⚠️ Could not find group to restore: $seriesName")
                }
            } else {
                Log.w(
                    TAG,
                    "⚠️ Group already correctly selected: ${_selectedGroup.value?.seriesName}"
                )
            }
        } ?: run {
            Log.w(TAG, "⚠️ No persistent selected group to restore")
        }
    }

    fun refreshProducts() {
        loadProducts(_currentProductCategoryId.value, forceRefresh = true)
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun selectProductGroup(group: ProductByGroup) {
        _selectedGroup.value = group
        _filteredProducts.value = group.products
        _persistentSelectedSeriesName = group.seriesName
    }

    fun clearSelectedGroup() {
        _selectedGroup.value = null
        _filteredProducts.value = emptyList()
        _persistentSelectedSeriesName = null
    }

    fun restoreSelectedGroup() {
        restoreSelectedGroupIfNeeded()
    }

    fun filterProductsByPrefix(prefix: String) {
        viewModelScope.launch {
            try {
                productUseCases.getProductsByNamePrefix(prefix).collect { products ->
                    _filteredProducts.value = products
                }
            } catch (_: Exception) {
                _filteredProducts.value = emptyList()
            }
        }
    }

    fun filterProductsByCategoryName(categoryName: String) {
        viewModelScope.launch {
            try {
                val products = (productsState.value as? SafeApiResult.Success)?.data ?: emptyList()
                _filteredProducts.value = products.filter { it.categoryName == categoryName }
            } catch (_: Exception) {
                _filteredProducts.value = emptyList()
            }
        }
    }

    fun filterProductsByCategoryType(categoryType: String) {
        viewModelScope.launch {
            try {
                val products = (productsState.value as? SafeApiResult.Success)?.data ?: emptyList()
                _filteredProducts.value = products.filter { it.categoryType == categoryType }
            } catch (_: Exception) {
                _filteredProducts.value = emptyList()
            }
        }
    }

    suspend fun getProductById(id: String): Product? {
        return productUseCases.getProduct(id)
    }

    fun getProductStatistics(): ProductStatistics {
        val products = (productsState.value as? SafeApiResult.Success)?.data ?: emptyList()
        val ready = products.count { it.status == STATISTICS_READY }
        val research = products.count { it.status == STATISTICS_RESEARCH }
        val initiation = products.count { it.status == STATISTICS_INITIATION }
        return ProductStatistics(
            total = products.size,
            ready = ready,
            research = research,
            initiation = initiation
        )
    }

    fun getActionInfo(language: String = ENGLISH): String {
        val products = (_productsState.value as? SafeApiResult.Success)?.data?.firstOrNull()
        return if (language == INDONESIAN) {
            products?.actionInfo?.information?.indonesian ?: ""
        } else {
            products?.actionInfo?.information?.english ?: ""
        }
    }
}