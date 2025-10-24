package com.arkhe.menu.presentation.viewmodel

import app.cash.turbine.test
import com.arkhe.menu.MainCoroutineRule
import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.domain.usecase.auth.ActivationUseCase
import com.arkhe.menu.domain.usecase.auth.ActivationUseCases
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Deklarasikan mock untuk use case spesifik
    private lateinit var activationUseCase: ActivationUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    private val fakeUserId = "testUser"
    private val fakePhone = "08123456789"
    private val fakeMail = "test@example.com"
    private val stepVerification = "verification" // Sesuaikan dengan step yang Anda gunakan

    @Before
    fun setUp() {
        // Mock use case individu, lalu bungkus dalam data class
        activationUseCase = mockk()
        val activationUseCases = ActivationUseCases(activationStepUseCase = activationUseCase)

        authRepository = mockk(relaxed = true)

        // Mock flow yang ada di repository
        coEvery { authRepository.isActivatedFlow } returns flowOf(false)
        coEvery { authRepository.isSignedInFlow } returns flowOf(false)

        viewModel = AuthViewModel(activationUseCases, authRepository)
    }

    // --- TEST KASUS POSITIF ---
    @Test
    fun `performActivationStep - when use case returns Success - updates state to Success`() =
        runTest {
            // ARRANGE: Siapkan data palsu yang akan dikembalikan oleh use case
            val successMessage = "Verification successful"
            val successResponse = ActivationResponse(status = "success", message = successMessage)
            val successResult = flowOf(SafeResourceResult.Success(successResponse))

            // Mock pemanggilan use case `activation`
            every {
                activationUseCase.invoke(
                    step = stepVerification,
                    userId = fakeUserId,
                    mail = fakeMail,
                    phone = fakePhone,
                    activationCode = null,
                    newPassword = null,
                    sessionActivation = null,
                    isPinActive = null
                )
            } returns successResult

            // ACT & ASSERT
            viewModel.uiState.test {
                // 1. State awal adalah Idle
                assertEquals(AuthUiState.Idle, awaitItem())

                // Panggil fungsi di ViewModel
                viewModel.performActivationStep(
                    step = stepVerification,
                    userId = fakeUserId,
                    phone = fakePhone,
                    mail = fakeMail
                )

                // 2. State berubah menjadi Loading
                assertEquals(AuthUiState.Loading, awaitItem())

                // 3. State akhir adalah Success
                val finalState = awaitItem()
                assertTrue(finalState is AuthUiState.Success)
                assertEquals(successMessage, (finalState as AuthUiState.Success).message)
                assertEquals(SuccessType.ACTIVATION, finalState.type)

                // Pastikan tidak ada emisi state lain
                cancelAndIgnoreRemainingEvents()
            }
        }

    // --- TEST KASUS NEGATIF ---
    @Test
    fun `performActivationStep - when use case returns Error - updates state to Error`() =
        runTest {
            // ARRANGE
            val errorMessage = "User ID not found"
            val failureResult = flowOf(SafeResourceResult.Failure<ActivationResponse>(errorMessage))

            // Mock pemanggilan use case `activation` untuk skenario gagal
            every {
                activationUseCase.invoke(
                    step = stepVerification,
                    userId = fakeUserId,
                    mail = fakeMail,
                    phone = fakePhone,
                    activationCode = null,
                    newPassword = null,
                    sessionActivation = null,
                    isPinActive = null
                )
            } returns failureResult

            // ACT & ASSERT
            viewModel.uiState.test {
                // 1. State awal adalah Idle
                assertEquals(AuthUiState.Idle, awaitItem())

                // Panggil fungsi di ViewModel
                viewModel.performActivationStep(
                    step = stepVerification,
                    userId = fakeUserId,
                    phone = fakePhone,
                    mail = fakeMail
                )

                // 2. State berubah menjadi Loading
                assertEquals(AuthUiState.Loading, awaitItem())

                // 3. State akhir adalah Error
                val finalState = awaitItem()
                assertTrue(finalState is AuthUiState.Failed)
                assertEquals(errorMessage, (finalState as AuthUiState.Failed).message)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
