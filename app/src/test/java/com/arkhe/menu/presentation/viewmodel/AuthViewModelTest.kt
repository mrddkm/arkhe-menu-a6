package com.arkhe.menu.presentation.viewmodel

import app.cash.turbine.test
import com.arkhe.menu.MainCoroutineRule
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.auth.Verification
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.domain.usecase.auth.ActivationUseCases
import io.mockk.coEvery
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

    private lateinit var activationUseCases: ActivationUseCases
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    private val fakeUserId = "testUser"
    private val fakePhone = "08123456789"
    private val fakeMail = "test@example.com"
    private val successVerification = Verification(userId = fakeUserId, name = "Test User")

    @Before
    fun setUp() {
        activationUseCases = mockk()
        authRepository = mockk(relaxed = true)

        coEvery { authRepository.isActivatedFlow } returns flowOf(false)
        coEvery { authRepository.isSignedInFlow } returns flowOf(false)

        viewModel = AuthViewModel(activationUseCases, authRepository)
    }

    // --- TEST KASUS POSITIF ---
    @Test
    fun `requestVerification - when use case returns Success - updates state to Success`() =
        runTest {
            val successResult = SafeApiResult.Success(successVerification)
            coEvery { activationUseCases.verification(any(), any(), any()) } returns successResult

            viewModel.verificationState.test {
                // PERBAIKAN: Panggil fungsi DULU
                viewModel.requestVerification(fakeUserId, fakePhone, fakeMail)

                // 1. State awal saat mulai mengamati adalah 'Loading' dari inisialisasi ViewModel.
                assertEquals(SafeApiResult.Loading, awaitItem())

                // 2. State berikutnya adalah 'Success' setelah fungsi selesai.
                val finalState = awaitItem()
                assertTrue(finalState is SafeApiResult.Success)
                assertEquals(successVerification, (finalState as SafeApiResult.Success).data)

                // Pastikan tidak ada emisi state lain
                cancelAndIgnoreRemainingEvents()
            }
        }

    // --- TEST KASUS NEGATIF ---
    @Test
    fun `requestVerification - when use case returns Failure - updates state to Failure`() =
        runTest {
            val errorMessage = "Network connection lost"
            val failureResult = SafeApiResult.Failure(Exception(errorMessage))
            coEvery { activationUseCases.verification(any(), any(), any()) } returns failureResult

            viewModel.verificationState.test {
                // PERBAIKAN: Panggil fungsi DULU
                viewModel.requestVerification(fakeUserId, fakePhone, fakeMail)

                // 1. State awal adalah 'Loading'.
                assertEquals(SafeApiResult.Loading, awaitItem())

                // 2. State berikutnya adalah 'Failure' setelah fungsi selesai.
                val finalState = awaitItem()
                assertTrue(finalState is SafeApiResult.Failure)
                assertEquals(errorMessage, (finalState as SafeApiResult.Failure).exception.message)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
