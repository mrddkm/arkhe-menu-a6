package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.domain.usecase.product.ProductUseCases
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

    private val _productGroups = MutableStateFlow<List<ProductGroup>>(emptyList())
    val productGroups: StateFlow<List<ProductGroup>> = _productGroups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<ProductGroup?>(null)
    val selectedGroup: StateFlow<ProductGroup?> = _selectedGroup.asStateFlow()

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()

    private val _currentProductCategoryId = MutableStateFlow("ALL")
    val currentProductCategoryId: StateFlow<String> = _currentProductCategoryId.asStateFlow()

    private var isInitialized = false

    init {
        Log.d("init", "## ProductViewModel::initialized ##")
        Log.d(TAG, "  - categoryUseCases: $productUseCases")
        Log.d(TAG, "  - sessionManager: $sessionManager")

        viewModelScope.launch {
            try {
                productUseCases.getProducts(sessionManager.getTokenForApiCall())
                    .collectLatest { productsResult ->
                        Log.d(TAG, "📊 Products result: ${productsResult::class.simpleName}")
                        _productsState.value = productsResult

                        if (productsResult is SafeApiResult.Success && productsResult.data.isNotEmpty()) {
                            loadProductGroups()
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error in init: ${e.message}", e)
                _productsState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun loadProducts(
        productCategoryId: String = "ALL",
        forceRefresh: Boolean = false
    ) {
        Log.d(TAG, "========== loadProducts ==========")
        Log.d(TAG, "forceRefresh: $forceRefresh")

        if (!forceRefresh) {
            Log.d(TAG, "Using local DB data flow, no API call.")
            return
        }

        viewModelScope.launch {
            try {
                _currentProductCategoryId.value = productCategoryId
                _productsState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()
                Log.d(TAG, "🔑 Token from SessionManager: ${sessionToken.take(8)}...")

                val result = productUseCases.refreshProducts(sessionToken, productCategoryId)
                when (result) {
                    is SafeApiResult.Loading -> {
                        Log.d(TAG, "⏳ Products loading...")
                        _productsState.value = result
                    }

                    is SafeApiResult.Success -> {
                        Log.d(TAG, "✅ Products loaded successfully: ${result.data.size} items")
                        result.data.forEach { product ->
                            Log.d(TAG, "📋 Category: ${product.productDestination}")
                        }
                        _productsState.value = result
                        loadProductGroups()
                    }

                    is SafeApiResult.Error -> {
                        Log.e(TAG, "❌ Error loading categories: ${result.exception.message}")
                        _productsState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception in loadCategories: ${e.message}", e)
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
            Log.d(
                TAG,
                "🔄 Token error detected, refreshing and retrying..."
            )
            viewModelScope.launch {
                try {
                    val newToken = sessionManager.ensureTokenAvailable()
                    Log.d(
                        TAG,
                        "🔑 Retrying with refreshed token: ${newToken.take(8)}..."
                    )
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
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_productsState.value::class.simpleName}"
        )

        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    val token = sessionManager.getTokenForApiCall()
                    val productsResult = productUseCases.getProducts(token).firstOrNull()

                    when (productsResult) {
                        is SafeApiResult.Success -> {
                            if (productsResult.data.isEmpty()) {
                                Log.d(TAG, "🆕 No local data found, syncing categories from API...")
                                loadProducts(_currentProductCategoryId.value, forceRefresh = true)
                            } else {
                                Log.d(TAG, "🆗 Local data found, no need to sync.")
                                _productsState.value = productsResult
                                loadProductGroups()
                            }
                        }

                        is SafeApiResult.Error -> {
                            Log.e(
                                TAG,
                                "❌ Error getting categories: ${productsResult.exception.message}"
                            )
                            _productsState.value = productsResult
                        }

                        SafeApiResult.Loading -> {
                            Log.d(TAG, "🆗 Loading...")
                        }

                        null -> {
                            Log.d(
                                TAG,
                                "🆗 null result, loading fresh data..."
                            )
                            loadProducts(_currentProductCategoryId.value, forceRefresh = true)
                        }
                    }
                    isInitialized = true
                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "❌ Exception in ensureDataLoaded: ${e.message}",
                        e
                    )
                    _productsState.value = SafeApiResult.Error(e)
                }
            }
        } else {
            when (_productsState.value) {
                is SafeApiResult.Error -> {
                    Log.d(TAG, "🔄 Retrying after error...")
                    loadProducts(_currentProductCategoryId.value, forceRefresh = false)
                }

                is SafeApiResult.Loading -> {
                    Log.d(TAG, "⏳ Already loading, waiting...")
                }

                else -> {
                    Log.d(TAG, "✅ Data already loaded")
                    if (_productGroups.value.isEmpty()) {
                        Log.d(TAG, "🔄 Product groups not loaded, loading now...")
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
                _productGroups.value = groups
            } catch (_: Exception) {
                _productGroups.value = emptyList()
            }
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

    fun selectProductGroup(group: ProductGroup) {
        _selectedGroup.value = group
        _filteredProducts.value = group.products
    }

    fun clearSelectedGroup() {
        _selectedGroup.value = null
        _filteredProducts.value = emptyList()
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

    suspend fun getProductById(id: String): Product? {
        return productUseCases.getProduct(id)
    }

    fun getActionInfo(language: String = "en"): String {
        val products = (_productsState.value as? SafeApiResult.Success)?.data?.firstOrNull()
        return if (language == "id") {
            products?.actionInfo?.information?.indonesian ?: ""
        } else {
            products?.actionInfo?.information?.english ?: ""
        }
    }
}