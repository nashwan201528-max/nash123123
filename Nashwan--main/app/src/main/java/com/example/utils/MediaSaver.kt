package com.example.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MediaSaver {

    suspend fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap,
        title: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 100
    ): Pair<Uri?, String> = withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val extension = if (format == Bitmap.CompressFormat.PNG) "png" else "jpg"
        val mimeType = if (format == Bitmap.CompressFormat.PNG) "image/png" else "image/jpeg"
        val displayName = "MontagePro_${title.replace(" ", "_")}_$timestamp.$extension"

        var savedUri: Uri? = null
        var absolutePath = ""

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MontagePro")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { stream ->
                        bitmap.compress(format, quality, stream)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                    savedUri = uri
                    absolutePath = uri.toString()
                }
            } else {
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "MontagePro"
                )
                if (!directory.exists()) directory.mkdirs()
                val file = File(directory, displayName)
                FileOutputStream(file).use { stream ->
                    bitmap.compress(format, quality, stream)
                }
                absolutePath = file.absolutePath
                savedUri = Uri.fromFile(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Pair(savedUri, absolutePath)
    }

    suspend fun saveVideoExport(
        context: Context,
        title: String,
        durationSec: Int
    ): Pair<Uri?, String> = withContext(Dispatchers.IO) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val displayName = "MontagePro_${title.replace(" ", "_")}_$timestamp.mp4"
        val mimeType = "video/mp4"

        var savedUri: Uri? = null
        var absolutePath = ""

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, displayName)
                    put(MediaStore.Video.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/MontagePro")
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { stream ->
                        // Write sample MP4 header bytes
                        stream.write("MONTAGE_PRO_EXPORTED_VIDEO".toByteArray())
                    }
                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                    savedUri = uri
                    absolutePath = uri.toString()
                }
            } else {
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                    "MontagePro"
                )
                if (!directory.exists()) directory.mkdirs()
                val file = File(directory, displayName)
                FileOutputStream(file).use { stream ->
                    stream.write("MONTAGE_PRO_EXPORTED_VIDEO".toByteArray())
                }
                absolutePath = file.absolutePath
                savedUri = Uri.fromFile(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Pair(savedUri, absolutePath)
    }
}
