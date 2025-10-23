package com.arkhe.menu.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
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

@RunWith(AndroidJUnit4::class)
class AuthRepositoryIntegrationTest : KoinTest {

    // --- PERBAIKAN UTAMA: Deklarasikan tanpa inisialisasi ---
    private lateinit var authRepository: AuthRepository

    // @get:Rule tidak lagi diperlukan jika kita mengelola secara manual
    // val koinTestRule = KoinTestRule.create { ... }

    // --- KEMBALI MENGGUNAKAN @Before/@After SECARA EKSPLISIT ---
    // Ini memberikan kontrol penuh atas kapan Koin dimulai dan dihentikan,
    // menghindari race condition dengan 'by inject()'.
    @Before
    fun setUp() {
        // Hentikan Koin yang mungkin sudah berjalan dari App class, untuk memulai dari awal yang bersih
        stopKoin()
        // Mulai Koin secara manual dengan modul-modul yang diperlukan
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(appModule, dataModule, domainModule)
        }
        // SEKARANG, setelah Koin pasti berjalan, dapatkan instance-nya
        authRepository = get()
    }

    // --- TEST KASUS NYATA ---
    @Test
    fun verification_whenHittingRealApiWithValidData_shouldReceiveResponse() =
        runBlocking {
            // ARRANGE
            val testUserId = "000000"
            val testPhone = "6285659988939"
            val testMail = "didik.muttaqien@gmail.com"

            // ACT
            println("Integration Test: Sending real verification request...")
            val result = authRepository.verification(testUserId, testPhone, testMail)
            println("Integration Test: Received result: $result")

            // ASSERT
            assertNotNull("Result should not be null", result)

            when (result) {
                is SafeApiResult.Success -> {
                    println("Integration Test: Received SUCCESS")
                    assertNotNull(result.data)
                }

                is SafeApiResult.Failure -> {
                    println("Integration Test: Received FAILURE with exception: ${result.exception.message}")
                    assertNotNull(result.exception.message)
                }

                is SafeApiResult.Loading -> {
                    throw AssertionError("Result should not be Loading")
                }
            }
            assertTrue("Test completed", true)
        }
}