package com.arkhe.menu.data.local.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageStorageManager(private val context: Context) {

    companion object {
        private const val TAG = "ImageStorageManager"
        private const val IMAGES_FOLDER = "cached_images"
        private const val MAX_IMAGE_SIZE = 2048
        private const val QUALITY = 85
    }

    private val imagesDirectory: File by lazy {
        File(context.filesDir, IMAGES_FOLDER).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    /**
     * Download and save image from URL
     * @param imageUrl Original Google Drive or any image URL
     * @param fileName Unique filename for the image (without extension)
     * @return Local file path if success, null if failed
     */
    suspend fun downloadAndSaveImage(imageUrl: String, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting download for: $imageUrl")

                val directUrl = convertToDirectDownloadUrl(imageUrl)
                Log.d(TAG, "Direct URL: $directUrl")

                // Deteksi format dari URL asli terlebih dahulu, bukan dari direct URL
                val formatFromOriginalUrl = detectImageFormat(imageUrl)
                Log.d(TAG, "Format from original URL: $formatFromOriginalUrl")

                // Download bitmap untuk mendapat format sebenarnya
                val bitmap = downloadBitmap(directUrl)
                if (bitmap == null) {
                    Log.e(TAG, "Failed to download bitmap from: $directUrl")
                    return@withContext null
                }

                // Deteksi format dari bitmap yang sudah di-download
                val actualFormat = detectFormatFromBitmap(bitmap)
                Log.d(TAG, "Actual format from bitmap: $actualFormat")

                // Prioritas format: dari bitmap actual > dari URL asli > default jpg
                val finalFormat = actualFormat ?: formatFromOriginalUrl ?: "jpg"

                val ext = when (finalFormat) {
                    "png" -> "png"
                    "webp" -> "webp"
                    else -> "jpg"
                }
                Log.d(TAG, "Final format: $finalFormat")
                Log.d(TAG, "Final extension: $ext")

                val preserveFormat = ext == "png" || ext == "webp"

                val localFile = File(imagesDirectory, "$fileName.$ext")
                if (localFile.exists() && localFile.length() > 0) {
                    Log.d(TAG, "Image already exists: ${localFile.absolutePath}")
                    bitmap.recycle()
                    return@withContext localFile.absolutePath
                }

                val optimizedBitmap = optimizeBitmap(bitmap)
                val saved = saveBitmapToFile(optimizedBitmap, localFile, preserveFormat)

                bitmap.recycle()
                if (bitmap != optimizedBitmap) {
                    optimizedBitmap.recycle()
                }

                if (saved) {
                    Log.d(TAG, "Image saved successfully: ${localFile.absolutePath}")
                    return@withContext localFile.absolutePath
                }

                Log.e(TAG, "Failed to save image: $imageUrl")
                null
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading image: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Get local image path if exists
     */
    fun getLocalImagePath(fileName: String): String? {
        val extensions = listOf("jpg", "png", "webp")
        for (ext in extensions) {
            val localFile = File(imagesDirectory, "$fileName.$ext")
            if (localFile.exists() && localFile.length() > 0) {
                return localFile.absolutePath
            }
        }
        return null
    }

    /**
     * Delete cached image
     */
    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val extensions = listOf("jpg", "png", "webp")
                var allDeleted = true
                for (ext in extensions) {
                    val localFile = File(imagesDirectory, "$fileName.$ext")
                    if (localFile.exists()) {
                        allDeleted = localFile.delete() && allDeleted
                    }
                }
                allDeleted
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting image: ${e.message}", e)
                false
            }
        }
    }

    /**
     * Clear all cached images
     */
    suspend fun clearAllImages(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                imagesDirectory.listFiles()?.forEach { file ->
                    file.delete()
                }
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing images: ${e.message}", e)
                false
            }
        }
    }

    /**
     * Get cache size in bytes
     */
    suspend fun getCacheSize(): Long {
        return withContext(Dispatchers.IO) {
            try {
                imagesDirectory.listFiles()?.sumOf { it.length() } ?: 0L
            } catch (_: Exception) {
                0L
            }
        }
    }

    /**
     * Convert Google Drive view URL to direct download URL
     */
    private fun convertToDirectDownloadUrl(url: String): String {
        return when {
            url.contains("drive.google.com/file/d/") -> {
                // Extract file ID from Google Drive URL
                val fileId = url.substringAfter("file/d/").substringBefore("/")
                "https://drive.google.com/uc?id=$fileId&export=download"
            }

            url.contains("drive.google.com/open?id=") -> {
                val fileId = url.substringAfter("id=")
                "https://drive.google.com/uc?id=$fileId&export=download"
            }

            else -> url // Return original URL if not Google Drive
        }
    }

    /**
     * Download bitmap from URL with Content-Type header check
     */
    private suspend fun downloadBitmap(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(url).openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 10000
                    readTimeout = 15000
                    doInput = true
                    setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
                }

                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    // Log Content-Type untuk debugging
                    val contentType = connection.getHeaderField("Content-Type")
                    Log.d(TAG, "Content-Type: $contentType")

                    connection.inputStream.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                } else {
                    Log.e(TAG, "HTTP error: ${connection.responseCode}")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading bitmap: ${e.message}", e)
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

    /**
     * Optimize bitmap for storage
     */
    private fun optimizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // If image is already small enough, return original
        if (width <= MAX_IMAGE_SIZE && height <= MAX_IMAGE_SIZE) {
            return bitmap
        }

        // Calculate new dimensions
        val ratio = minOf(
            MAX_IMAGE_SIZE.toFloat() / width,
            MAX_IMAGE_SIZE.toFloat() / height
        )

        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return bitmap.scale(newWidth, newHeight)
    }

    /**
     * Detect image format from URL (original URL before conversion)
     */
    private fun detectImageFormat(url: String): String? {
        return when {
            url.contains(".png", ignoreCase = true) -> "png"
            url.contains(".jpg", ignoreCase = true) || url.contains(".jpeg", ignoreCase = true) -> "jpg"
            url.contains(".webp", ignoreCase = true) -> "webp"
            url.contains(".gif", ignoreCase = true) -> "gif"
            else -> null
        }
    }

    /**
     * Detect format from bitmap configuration
     * Ini metode tambahan untuk mendeteksi format dari bitmap
     */
    private fun detectFormatFromBitmap(bitmap: Bitmap): String? {
        return try {
            // PNG biasanya memiliki alpha channel
            when {
                bitmap.hasAlpha() -> {
                    Log.d(TAG, "Bitmap has alpha channel, likely PNG")
                    "png"
                }
                else -> {
                    Log.d(TAG, "Bitmap has no alpha channel")
                    null // Tidak bisa dipastikan, biarkan logika lain yang menentukan
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting format from bitmap: ${e.message}")
            null
        }
    }

    /**
     * Enhanced method to detect format from HTTP response headers
     */
    private suspend fun detectFormatFromHeaders(url: String): String? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(url).openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 5000
                    readTimeout = 5000
                    requestMethod = "HEAD" // Only get headers
                    setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
                }

                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val contentType = connection.getHeaderField("Content-Type")
                    Log.d(TAG, "Content-Type from headers: $contentType")

                    when {
                        contentType?.contains("png", ignoreCase = true) == true -> "png"
                        contentType?.contains("jpeg", ignoreCase = true) == true -> "jpg"
                        contentType?.contains("webp", ignoreCase = true) == true -> "webp"
                        contentType?.contains("gif", ignoreCase = true) == true -> "gif"
                        else -> null
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting headers: ${e.message}")
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

    /**
     * Save bitmap to file with format preservation option
     */
    private fun saveBitmapToFile(
        bitmap: Bitmap,
        file: File,
        preserveFormat: Boolean = false
    ): Boolean {
        return try {
            FileOutputStream(file).use { out ->
                when {
                    preserveFormat && file.extension.equals("png", true) -> {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }

                    preserveFormat && file.extension.equals("webp", true) &&
                            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R -> {
                        bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, out)
                    }

                    preserveFormat && file.extension.equals("webp", true) -> {
                        bitmap.compress(Bitmap.CompressFormat.WEBP, QUALITY, out)
                    }

                    else -> {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, out)
                    }
                }
            }
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error saving bitmap: ${e.message}", e)
            false
        }
    }
}