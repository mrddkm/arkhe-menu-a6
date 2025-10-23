package com.arkhe.menu.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.remote.api.Resource
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.auth.Verification
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.domain.usecase.auth.ActivationUseCases
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

enum class SuccessType { ACTIVATION, SIGNEDIN }

class AuthViewModel(
    private val activationUseCases: ActivationUseCases,
    private val repo: AuthRepository
) : ViewModel() {

    private val _sessionActivation = mutableStateOf<String?>(null)

    fun performActivationStep(
        step: String,
        userId: String? = null,
        mail: String? = null,
        phone: String? = null,
        activationCode: String? = null,
        newPassword: String? = null,
        isPinActive: Boolean? = null
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Gunakan session yang sudah tersimpan untuk step selanjutnya
            val session = _sessionActivation.value

            // Panggil UseCase (yang akan kita buat/modifikasi)
            activationUseCases.activation(
                step = step,
                userId = userId,
                mail = mail,
                phone = phone,
                activationCode = activationCode,
                newPassword = newPassword,
                sessionActivation = session,
                isPinActive = isPinActive
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Simpan sessionActivation jika ada di response
                        result.data?.sessionActivation?.let { newSession ->
                            _sessionActivation.value = newSession
                        }

                        _uiState.value = AuthUiState.Success(
                            type = SuccessType.ACTIVATION,
                            // Sesuaikan message berdasarkan response dari API
                            message = result.data?.message ?: "Success"
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value =
                            AuthUiState.Error(result.message ?: "An unknown error occurred")
                    }

                    is Resource.Loading -> {
                        _uiState.value = AuthUiState.Loading
                    }
                }
            }
        }
    }


    /*DELETE IF STABLE*/
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isActivatedFlow = repo.isActivatedFlow
    val isSignedInFlow = repo.isSignedInFlow

    private val _verificationState =
        MutableStateFlow<SafeApiResult<Verification>>(SafeApiResult.Loading)
    val verificationState: StateFlow<SafeApiResult<Verification>> = _verificationState.asStateFlow()

    /*    fun requestVerification(userId: String, phone: String, mail: String) {
            viewModelScope.launch {
                _verificationState.value = SafeApiResult.Loading

                val verificationResult = activationUseCases.verification(userId, phone, mail)

                _verificationState.value = verificationResult
            }
        }*/

    /*    fun verifyActivationCode(code: String) {
            viewModelScope.launch {
                _uiState.value = AuthUiState.Loading
                val res = repo.verifyActivationCode(code)
                _uiState.value = res.fold(
                    onSuccess = { AuthUiState.Success(it, SuccessType.ACTIVATION) },
                    onFailure = { AuthUiState.Error(it.message ?: "Invalid code") }
                )
            }
        }*/

    /*    fun createPassword(password: String) {
            viewModelScope.launch {
                _uiState.value = AuthUiState.Loading
                val res = repo.createPassword(password)
                _uiState.value = res.fold(
                    onSuccess = { AuthUiState.Success(it, SuccessType.ACTIVATION) },
                    onFailure = { AuthUiState.Error(it.message ?: "Weak password") }
                )
            }
        }*/

    /*    fun signedIn(userId: String, password: String) {
            viewModelScope.launch {
                _uiState.value = AuthUiState.Loading
                val res = repo.signIn(userId, password)
                _uiState.value = res.fold(
                    onSuccess = {
                        repo.setSignedIn(true)
                        AuthUiState.Success(it, SuccessType.SIGNEDIN)
                    },
                    onFailure = { AuthUiState.Error(it.message ?: "SignIn failed") }
                )
            }
        }*/

    /*    fun savePin(pin: String) {
            viewModelScope.launch {
                repo.savePinHashed(pin)
                repo.setActivated(true)
                _uiState.value =
                    AuthUiState.Success("PIN saved & activation complete", SuccessType.ACTIVATION)
            }
        }*/

    suspend fun unlockWithPin(pin: String): Boolean = repo.checkPin(pin)
    fun incrementPinAttempts() = viewModelScope.launch { repo.incrementPinAttempts() }
    fun resetPinAttempts() = viewModelScope.launch { repo.resetPinAttempts() }
    suspend fun getPinAttempts(): Int = repo.getPinAttempts()

    fun deactivatedAuthState() {
        viewModelScope.launch {
            repo.deactivatedAuthState()
            _uiState.value =
                AuthUiState.Success("Deactivated complete", SuccessType.ACTIVATION)
        }
    }

    fun signedOutAuthState() {
        viewModelScope.launch {
            repo.signedOutAuthState()
            _uiState.value =
                AuthUiState.Success("SignedOut complete", SuccessType.SIGNEDIN)
        }
    }
}
