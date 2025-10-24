package com.arkhe.menu.presentation.viewmodel

import app.cash.turbine.test
import com.arkhe.menu.MainCoroutineRule
import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.domain.usecase.auth.ActivationUseCase
import com.arkhe.menu.domain.usecase.auth.ActivationUseCases
import com.arkhe.menu.domain.usecase.auth.SignInUseCase
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

    private lateinit var activationUseCase: ActivationUseCase
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    private val fakeUserId = "testUser"
    private val fakePhone = "08123456789"
    private val fakeMail = "test@example.com"
    private val stepVerification = "verification"

    @Before
    fun setUp() {
        activationUseCase = mockk()
        signInUseCase = mockk()
        val activationUseCases = ActivationUseCases(
            activationStepUseCase = activationUseCase,
            signInUseCase = signInUseCase

        )

        authRepository = mockk(relaxed = true)

        coEvery { authRepository.isActivatedFlow } returns flowOf(false)
        coEvery { authRepository.isSignedInFlow } returns flowOf(false)

        viewModel = AuthViewModel(activationUseCases, authRepository)
    }

    @Test
    fun `performActivationStep - when use case returns Success - updates state to Success`() =
        runTest {
            // ARRANGE
            val successMessage = "Verification successful"
            val successResponse = ActivationResponse(status = "success", message = successMessage)
            // Hasil dari UseCase adalah Flow yang berisi Loading, lalu Success
            val successResult = flowOf(
                SafeResourceResult.Loading(),
                SafeResourceResult.Success(successResponse)
            )

            // Mock pemanggilan use case `invoke`.
            // 'any()' digunakan untuk menyederhanakan dan menghindari error jika parameter bertambah.
            every {
                activationUseCase.invoke(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
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

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `performActivationStep - when use case returns Failure - updates state to Failed`() =
        runTest {
            // ARRANGE
            val errorMessage = "User ID not found"
            // Pastikan menggunakan nama 'Failure' yang benar sesuai dengan SafeResourceResult
            val failureResult = flowOf(
                SafeResourceResult.Loading(),
                SafeResourceResult.Failure<ActivationResponse>(errorMessage)
            )

            // Mock pemanggilan use case `invoke` untuk skenario gagal
            every {
                activationUseCase.invoke(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
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

                // 3. State akhir adalah Failed
                val finalState = awaitItem()
                assertTrue(finalState is AuthUiState.Failed)
                assertEquals(errorMessage, (finalState as AuthUiState.Failed).message)

                cancelAndIgnoreRemainingEvents()
            }
        }
}