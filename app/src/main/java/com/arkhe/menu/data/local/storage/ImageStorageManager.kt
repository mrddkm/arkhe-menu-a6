package com.arkhe.menu.data.local.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.graphics.scale

class ImageStorageManager(private val context: Context) {

    companion object {
        private const val TAG = "ImageStorageManager"
        private const val IMAGES_FOLDER = "cached_images"
        private const val MAX_IMAGE_SIZE = 2048 // Max width/height in pixels
        private const val QUALITY = 85 // JPEG quality
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

                // Convert Google Drive view link to direct download link
                val directUrl = convertToDirectDownloadUrl(imageUrl)
                Log.d(TAG, "Direct URL: $directUrl")

                // Check if file already exists
                val localFile = File(imagesDirectory, "$fileName.jpg")
                if (localFile.exists() && localFile.length() > 0) {
                    Log.d(TAG, "Image already exists: ${localFile.absolutePath}")
                    return@withContext localFile.absolutePath
                }

                // Download image
                val bitmap = downloadBitmap(directUrl)
                if (bitmap != null) {
                    // Optimize and save bitmap
                    val optimizedBitmap = optimizeBitmap(bitmap)
                    val saved = saveBitmapToFile(optimizedBitmap, localFile)

                    bitmap.recycle()
                    if (bitmap != optimizedBitmap) {
                        optimizedBitmap.recycle()
                    }

                    if (saved) {
                        Log.d(TAG, "Image saved successfully: ${localFile.absolutePath}")
                        return@withContext localFile.absolutePath
                    }
                }

                Log.e(TAG, "Failed to download or save image: $imageUrl")
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
        val localFile = File(imagesDirectory, "$fileName.jpg")
        return if (localFile.exists() && localFile.length() > 0) {
            localFile.absolutePath
        } else {
            null
        }
    }

    /**
     * Delete cached image
     */
    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val localFile = File(imagesDirectory, "$fileName.jpg")
                if (localFile.exists()) {
                    localFile.delete()
                } else {
                    true
                }
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
     * Download bitmap from URL
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
                    // Add user agent to avoid blocking
                    setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
                }

                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
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
     * Detect image format from URL
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
     * Save bitmap to file with format preservation option
     */
    private fun saveBitmapToFile(bitmap: Bitmap, file: File, preserveFormat: Boolean = false): Boolean {
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
                        // Default to JPEG for best compatibility and size
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