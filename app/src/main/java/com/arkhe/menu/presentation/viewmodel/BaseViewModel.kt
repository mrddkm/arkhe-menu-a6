package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.repository.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Domain>(
    private val repository: BaseRepository<*, Domain>
) : ViewModel() {

    private val _state =
        MutableStateFlow<SafeApiResult<List<Domain>>>(SafeApiResult.Loading)
    val state: StateFlow<SafeApiResult<List<Domain>>> = _state.asStateFlow()

    init {
        observeLocal()
    }

    private fun observeLocal() {
        viewModelScope.launch {
            repository.getAll().collect { data ->
                if (data.isNotEmpty()) {
                    _state.value = SafeApiResult.Success(data)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = SafeApiResult.Loading
            _state.value = repository.sync()
        }
    }
}