package com.arkhe.menu.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.remote.api.SafeResourceResult
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
    data class Failed(val message: String) : AuthUiState
}

enum class SuccessType { ACTIVATION, SIGNEDIN }

class AuthViewModel(
    private val activationUseCases: ActivationUseCases,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _sessionActivation = mutableStateOf<String?>(null)

    fun performActivationStep(
        step: String,
        userId: String? = null,
        mail: String? = null,
        phone: String? = null,
        activationCode: String? = null,
        newPassword: String? = null,
        isPinActive: Boolean? = null,
        deviceInfo: Map<String, String>? = null
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Gunakan session yang sudah tersimpan untuk step selanjutnya
            val session = _sessionActivation.value

            // Panggil UseCase (yang akan kita buat/modifikasi)
            activationUseCases.activationStepUseCase(
                step = step,
                userId = userId,
                mail = mail,
                phone = phone,
                activationCode = activationCode,
                newPassword = newPassword,
                sessionActivation = session,
                isPinActive = isPinActive,
                deviceId = deviceInfo?.get("deviceId"),
                manufacturer = deviceInfo?.get("manufacturer"),
                brand = deviceInfo?.get("brand"),
                model = deviceInfo?.get("model"),
                device = deviceInfo?.get("device"),
                product = deviceInfo?.get("product"),
                osVersion = deviceInfo?.get("osVersion"),
                sdkLevel = deviceInfo?.get("sdkLevel"),
                securityPatch = deviceInfo?.get("securityPatch"),
                deviceType = deviceInfo?.get("deviceType"),
                appVersionName = deviceInfo?.get("appVersionName"),
                appVersionCode = deviceInfo?.get("appVersionCode")
            ).collect { result ->
                when (result) {
                    is SafeResourceResult.Success -> {
                        result.data?.sessionActivation?.let { newSession ->
                            _sessionActivation.value = newSession
                        }

                        _uiState.value = AuthUiState.Success(
                            type = SuccessType.ACTIVATION,
                            message = result.data?.message ?: "Success"
                        )
                    }

                    is SafeResourceResult.Failure -> {
                        _uiState.value =
                            AuthUiState.Failed(result.message ?: "An unknown error occurred")
                    }

                    is SafeResourceResult.Loading -> {
                        _uiState.value = AuthUiState.Loading
                    }
                }
            }
        }
    }

// ... di dalam class AuthViewModel

    fun signIn(userId: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            // Panggil SignInUseCase yang baru
            activationUseCases.signInUseCase(userId, password).collect { result ->
                when (result) {
                    is SafeResourceResult.Success -> {
                        // Simpan status sign-in
                        authRepository.setSignedIn(true)

                        _uiState.value = AuthUiState.Success(
                            type = SuccessType.SIGNEDIN,
                            message = result.data?.message ?: "Sign-in successful"
                        )
                    }

                    is SafeResourceResult.Failure -> {
                        _uiState.value = AuthUiState.Failed(result.message ?: "Sign-in failed")
                    }

                    is SafeResourceResult.Loading -> {
                        _uiState.value = AuthUiState.Loading
                    }
                }
            }
        }
    }

    /*DELETE IF STABLE*/
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isActivatedFlow = authRepository.isActivatedFlow
    val isSignedInFlow = authRepository.isSignedInFlow

    suspend fun unlockWithPin(pin: String): Boolean = authRepository.checkPin(pin)
    fun incrementPinAttempts() = viewModelScope.launch { authRepository.incrementPinAttempts() }
    fun resetPinAttempts() = viewModelScope.launch { authRepository.resetPinAttempts() }
    suspend fun getPinAttempts(): Int = authRepository.getPinAttempts()

    fun deactivatedAuthState() {
        viewModelScope.launch {
            authRepository.deactivatedAuthState()
            _uiState.value =
                AuthUiState.Success("Deactivated complete", SuccessType.ACTIVATION)
        }
    }

    fun signedOutAuthState() {
        viewModelScope.launch {
            authRepository.signedOutAuthState()
            _uiState.value =
                AuthUiState.Success("SignedOut complete", SuccessType.SIGNEDIN)
        }
    }
}
