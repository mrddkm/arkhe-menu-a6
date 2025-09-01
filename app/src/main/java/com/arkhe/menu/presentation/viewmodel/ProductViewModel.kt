package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.domain.usecase.product.ProductUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productUseCases: ProductUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _productsState = MutableStateFlow<ApiResult<List<Product>>>(ApiResult.Loading)
    val productsState: StateFlow<ApiResult<List<Product>>> = _productsState.asStateFlow()

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

    init {
        loadProducts()
    }

    fun loadProducts(productCategoryId: String = "ALL", forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _currentProductCategoryId.value = productCategoryId
                val sessionToken = sessionManager.sessionToken.first()
                if (sessionToken != null) {
                    productUseCases.getProducts(sessionToken, productCategoryId, forceRefresh)
                        .collect { result ->
                            _productsState.value = result

                            // Update product groups when products are loaded
                            if (result is ApiResult.Success) {
                                loadProductGroups()
                            }
                        }
                } else {
                    _productsState.value = ApiResult.Error(Exception("No session token available"))
                }
            } catch (e: Exception) {
                _productsState.value = ApiResult.Error(e)
            }
        }
    }

    private fun loadProductGroups() {
        viewModelScope.launch {
            try {
                val groups = productUseCases.getProductGroups()
                _productGroups.value = groups
            } catch (e: Exception) {
                // Handle error if needed
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
            } catch (e: Exception) {
                _filteredProducts.value = emptyList()
            }
        }
    }

    suspend fun getProductById(id: String): Product? {
        return productUseCases.getProduct(id)
    }

    fun getActionInfo(language: String = "en"): String {
        val products = (_productsState.value as? ApiResult.Success)?.data?.firstOrNull()
        return if (language == "id") {
            products?.actionInfo?.information?.indonesian ?: ""
        } else {
            products?.actionInfo?.information?.english ?: ""
        }
    }
}