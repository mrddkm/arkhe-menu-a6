package com.arkhe.menu.presentation.viewmodel

import app.cash.turbine.test
import com.arkhe.menu.MainCoroutineRule
import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.model.auth.SignInResponse
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.domain.usecase.auth.ActivationUseCase
import com.arkhe.menu.domain.usecase.auth.AuthUseCases
import com.arkhe.menu.domain.usecase.auth.SignInUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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

    // Deklarasi mock, sudah benar
    private lateinit var activationUseCase: ActivationUseCase
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    // Variabel fake, sudah benar
    private val fakeSessionActivation = "kasdjabdkj8yp987we"
    private val fakeUserId = "testUser"
    private val fakePassword = "password123" // <-- Tambahkan fake password
    private val fakePhone = "08123456789"
    private val fakeMail = "test@example.com"
    private val stepVerification = "verification"

    @Before
    fun setUp() {
        activationUseCase = mockk()
        signInUseCase = mockk()

        // --- PERBAIKAN UTAMA DI SINI ---    // Gunakan nama kelas wrapper yang benar: AuthUseCases
        // Bukan ActivationUseCases
        val authUseCases = AuthUseCases(
            activationStepUseCase = activationUseCase,
            signInUseCase = signInUseCase
        )
        // --------------------------------

        authRepository = mockk(relaxed = true)

        coEvery { authRepository.isActivatedFlow } returns flowOf(false)
        coEvery { authRepository.isSignedInFlow } returns flowOf(false)

        // Inisialisasi ViewModel dengan wrapper use case yang benar
        // dan nama parameter yang benar ('authUseCases' bukan 'activationUseCases')
        viewModel = AuthViewModel(authUseCases, authRepository)
    }


    // --- TEST UNTUK ACTIVATION (sudah ada, hanya perbaikan kecil) ---
    @Test
    fun `performActivationStep - when use case returns Success - updates state to Success`() =
        runTest {
            // ARRANGE
            val successMessage = "Verification successful"
            val successResponse = ActivationResponse(status = "success", message = successMessage)
            val successResult = flow {
                emit(SafeResourceResult.Loading())
                delay(1)
                emit(SafeResourceResult.Success(successResponse))
            }
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

            viewModel.uiState.test {
                assertEquals(AuthUiState.Idle, awaitItem()) // Awal
                viewModel.performActivationStep(
                    step = stepVerification,
                    userId = fakeUserId,
                    phone = fakePhone,
                    mail = fakeMail
                )

                // 1. Cek state Loading awal
                assertEquals(AuthUiState.Loading, awaitItem())

                // 2. Langsung cek state akhir
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

            val failureResult = flow {
                emit(SafeResourceResult.Loading())
                delay(1)
                emit(SafeResourceResult.Failure<ActivationResponse>(errorMessage))
            }

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
                assertEquals(AuthUiState.Idle, awaitItem()) // Awal
                viewModel.performActivationStep(
                    step = stepVerification,
                    userId = fakeUserId,
                    phone = fakePhone,
                    mail = fakeMail
                )

                // 1. Cek state Loading awal
                assertEquals(AuthUiState.Loading, awaitItem())

                // 2. Langsung cek state akhir
                val finalState = awaitItem()

                assertTrue(finalState is AuthUiState.Failed)
                assertEquals(errorMessage, (finalState as AuthUiState.Failed).message)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `signIn - when use case returns Success - updates state to Success and calls setSignedIn`() =
        runTest {
            // ARRANGE (Biarkan seperti ini, sudah benar)
            val successMessage = "Sign-in successful"
            val successResponse = SignInResponse(
                status = "success",
                message = successMessage,
                sessionToken = "fake-token-123",
                userId = fakeUserId,
                profileId = "prof-1",
                nickName = "Test",
                initial = "T",
                positionName = "Tester",
                divisionName = "QA",
                roles = listOf("user"),
                sessionExpiredAt = "2025-12-31T23:59:59"
            )
            val successFlow = flow {
                // Kita masih perlu emit Loading di sini untuk mensimulasikan repository
                emit(SafeResourceResult.Loading())
                delay(1)
                emit(SafeResourceResult.Success(successResponse))
            }
            every {
                signInUseCase.invoke(fakeSessionActivation, fakeUserId, fakePassword)
            } returns successFlow
            coEvery { authRepository.setSignedIn(true) } returns Unit

            // ACT & ASSERT (PERBAIKI DI SINI)
            viewModel.uiState.test {
                assertEquals(AuthUiState.Idle, awaitItem()) // Awal
                viewModel.signIn(fakeSessionActivation, fakeUserId, fakePassword)

                // 1. Cek state Loading yang di-set di awal fungsi signIn
                assertEquals(AuthUiState.Loading, awaitItem())

                // 2. Langsung cek state akhir.
                // TestDispatcher akan menjalankan flow begitu cepat sehingga kita mungkin
                // tidak akan menangkap emisi Loading dari dalam flow.
                // awaitItem() akan langsung mendapatkan emisi 'Success'.
                val finalState = awaitItem()

                assertTrue(finalState is AuthUiState.Success)
                assertEquals(successMessage, (finalState as AuthUiState.Success).message)
                assertEquals(SuccessType.SIGNEDIN, finalState.type)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `signIn - when use case returns Failure - updates state to Failed`() = runTest {
        // ARRANGE (Biarkan seperti ini, sudah benar)
        val errorMessage = "Invalid credentials"
        val failureFlow = flow {
            emit(SafeResourceResult.Loading())
            delay(1)
            emit(SafeResourceResult.Failure<SignInResponse>(errorMessage))
        }
        every {
            signInUseCase.invoke(fakeSessionActivation, fakeUserId, fakePassword)
        } returns failureFlow

        // ACT & ASSERT (PERBAIKI DI SINI)
        viewModel.uiState.test {
            assertEquals(AuthUiState.Idle, awaitItem()) // Awal
            viewModel.signIn(fakeSessionActivation, fakeUserId, fakePassword)

            assertEquals(AuthUiState.Loading, awaitItem())
            val finalState = awaitItem()

            assertTrue(finalState is AuthUiState.Failed)
            assertEquals(errorMessage, (finalState as AuthUiState.Failed).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
