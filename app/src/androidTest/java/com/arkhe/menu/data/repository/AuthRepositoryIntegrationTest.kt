package com.arkhe.menu.data.repository

import androidx.room.Ignore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.utils.Constants
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
class AuthRepositoryIntegrationTest : KoinTest {

    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(appModule, dataModule, domainModule)
        }
        authRepository = get()
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    // --- TEST 1: SCENARIO POSITIF (SUCCESS) ---
    @Test
    fun verificationStep_withValidData_shouldReturnSuccess() = runBlocking {
        // ARRANGE: Gunakan data yang Anda tahu akan berhasil
        val testUserId = "230504" // <-- Data valid
        val testPhone = "6285659988939"
        val testMail = "didik.muttaqien@gmail.com"

        // ACT
        val resultFlow = authRepository.performActivationStep(
            step = Constants.ActivationFlow.ACT_VERIFICATION_STEP,
            userId = testUserId,
            phone = testPhone,
            mail = testMail,
            // ... parameter lain null ...
            activationCode = null, newPassword = null, sessionActivation = null, isPinActive = null,
            deviceId = null, manufacturer = null, brand = null, model = null, device = null,
            product = null, osVersion = null, sdkLevel = null, securityPatch = null,
            deviceType = null, appVersionName = null, appVersionCode = null
        )

        // ASSERT
        resultFlow.test(timeout = 15.seconds) {
            val result = awaitItem()
            println("POSITIVE TEST - Received: $result")

            // Ekspektasi: Harus Success
            assertTrue("Result should be Success, but was $result", result is SafeResourceResult.Success)

            val successResult = result as SafeResourceResult.Success
            assertNotNull("Success data should not be null", successResult.data)
            assertNotNull("On successful verification, userId must not be null", successResult.data?.userId)

            println("POSITIVE TEST - PASSED with User ID: ${successResult.data?.userId}")
            awaitComplete()
        }
    }

    // --- TEST 2: SCENARIO NEGATIF (FAILURE) ---
    @Ignore()
    @Test
    fun verificationStep_withInvalidData_shouldReturnFailure() = runBlocking {
        // ARRANGE: Gunakan data yang Anda tahu akan gagal
        val testUserId = "000000" // <-- Data tidak valid
        val testPhone = "6285659988939"
        val testMail = "didik.muttaqien@gmail.com"

        // ACT
        val resultFlow = authRepository.performActivationStep(
            step = Constants.ActivationFlow.ACT_VERIFICATION_STEP,
            userId = testUserId,
            phone = testPhone,
            mail = testMail,
            // ... parameter lain null ...
            activationCode = null, newPassword = null, sessionActivation = null, isPinActive = null,
            deviceId = null, manufacturer = null, brand = null, model = null, device = null,
            product = null, osVersion = null, sdkLevel = null, securityPatch = null,
            deviceType = null, appVersionName = null, appVersionCode = null
        )

        // ASSERT
        resultFlow.test(timeout = 15.seconds) {
            val result = awaitItem()
            println("NEGATIVE TEST - Received: $result")

            // Ekspektasi: Harus Failure
            assertTrue("Result should be Failure, but was $result", result is SafeResourceResult.Failure)

            val failureResult = result as SafeResourceResult.Failure
            assertNotNull("Failure message should not be null", failureResult.message)
            assertTrue(
                "Failure message should contain 'User ID not found'",
                failureResult.message?.contains("User ID not found", ignoreCase = true) == true
            )

            println("NEGATIVE TEST - PASSED with message: ${failureResult.message}")
            awaitComplete()
        }
    }
}
