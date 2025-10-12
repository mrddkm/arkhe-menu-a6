package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val message: String, val type: SuccessType) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

enum class SuccessType { ACTIVATION, LOGIN }

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun requestActivation(userId: String, phone: String, email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.requestActivation(userId, phone, email)
            _uiState.value = res.fold(
                onSuccess = { AuthUiState.Success(it, SuccessType.ACTIVATION) },
                onFailure = { AuthUiState.Error(it.message ?: "Activation failed") }
            )
        }
    }

    fun verifyActivationCode(code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.verifyActivationCode(code)
            _uiState.value = res.fold(
                onSuccess = { AuthUiState.Success(it, SuccessType.ACTIVATION) },
                onFailure = { AuthUiState.Error(it.message ?: "Invalid code") }
            )
        }
    }

    fun createPassword(password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.createPassword(password)
            _uiState.value = res.fold(
                onSuccess = { AuthUiState.Success(it, SuccessType.ACTIVATION) },
                onFailure = { AuthUiState.Error(it.message ?: "Weak password") }
            )
        }
    }

    fun signIn(userId: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val res = repo.signIn(userId, password)
            _uiState.value = res.fold(
                onSuccess = {
                    repo.setSignedIn(true)
                    AuthUiState.Success(it, SuccessType.LOGIN)
                },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun savePin(pin: String) {
        viewModelScope.launch {
            repo.savePinHashed(pin)
            repo.setActivated(true)
            _uiState.value =
                AuthUiState.Success("PIN saved & activation complete", SuccessType.ACTIVATION)
        }
    }

    suspend fun unlockWithPin(pin: String): Boolean = repo.checkPin(pin)

    fun incrementPinAttempts() = viewModelScope.launch { repo.incrementPinAttempts() }
    fun resetPinAttempts() = viewModelScope.launch { repo.resetPinAttempts() }
    suspend fun getPinAttempts(): Int = repo.getPinAttempts()

    suspend fun isActivated() = repo.isActivated()
    suspend fun isSignedIn() = repo.isSignedIn()
}
