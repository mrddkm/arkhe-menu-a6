package com.arkhe.menu.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings

object DeviceInfoUtil {
    @SuppressLint("HardwareIds")
    fun getDeviceInfo(@SuppressLint("HardwareIds") context: Context): Map<String, String> {
        val packageInfo = try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }

        val versionName = packageInfo?.versionName ?: "unknown"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo?.longVersionCode?.toString() ?: "unknown"
        } else {
            @Suppress("DEPRECATION")
            packageInfo?.versionCode?.toString() ?: "unknown"
        }

        return mapOf(
            "deviceId" to getDeviceId(context),
            "manufacturer" to Build.MANUFACTURER,
            "brand" to Build.BRAND,
            "model" to Build.MODEL,
            "device" to Build.DEVICE,
            "product" to Build.PRODUCT,
            "osVersion" to Build.VERSION.RELEASE,
            "sdkLevel" to versionNameForApi(Build.VERSION.SDK_INT),
            "securityPatch" to (Build.VERSION.SECURITY_PATCH ?: "unknown"),
            "deviceType" to getDeviceType(context),
            "appVersionName" to versionName,
            "appVersionCode" to versionCode
        )
    }

    fun getDeviceType(context: Context): String {
        val isTablet = context.resources.configuration.smallestScreenWidthDp >= 600
        return if (isTablet) "tablet" else "smartphone"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(@SuppressLint("HardwareIds") context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown"
    }

    @Suppress("SameParameterValue")
    private fun versionNameForApi(api: Int): String {
        return when (api) {
            23 -> "$api;Android 6.0 (Macadamia Nut Cookie/Marshmallow)"
            24 -> "$api;Android 7.0 (New York Cheesecake/Nougat)"
            25 -> "$api;Android 7.1 (New York Cheesecake/Nougat)"
            26 -> "$api;Android 8.0 (Oatmeal Cookie/Oreo)"
            27 -> "$api;Android 8.1 (Oatmeal Cookie/Oreo)"
            28 -> "$api;Android 9 (Pistachio Ice Cream/Pie)"
            29 -> "$api;Android 10 (Quince Tart)"
            30 -> "$api;Android 11 (Red Velvet Cake)"
            31, 32 -> "$api;Android 12 (Snow Cone)"
            33 -> "$api;Android 13 (Tiramisu)"
            34 -> "$api;Android 14 (Upside Down Cake)"
            35 -> "$api;Android 15 (Vanilla Ice Cream)"
            36 -> "$api;Android 16 (Baklava)"
            else -> "unknown version"
        }
    }
}
