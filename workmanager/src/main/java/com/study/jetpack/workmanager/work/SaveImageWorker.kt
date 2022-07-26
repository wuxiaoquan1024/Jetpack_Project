package com.study.jetpack.workmanager.work

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.study.jetpack.workmanager.KEY_IMAGE_URI
import com.study.jetpack.workmanager.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SaveImageWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val resolver = applicationContext.contentResolver
        return try {
            val file = File(inputData.getString(KEY_IMAGE_URI))
            val input = Uri.parse(inputData.getString(KEY_IMAGE_URI))
            val imageLocation = insertImage(resolver, input)
            if (imageLocation.isNullOrEmpty()) {
                Log.e(TAG, "Writing to MediaStore failed")
                Result.failure()
            }
            // Set the result of the worker by calling setOutputData().
            val output = Data.Builder()
                .putString(KEY_IMAGE_URI, imageLocation)
                .build()
            Result.success(output)
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception)
            Result.failure()
        }
    }

    private fun insertImage(resolver: ContentResolver, resourceUri: Uri): String? {
        val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(resourceUri))
        return MediaStore.Images.Media.insertImage(
            resolver, bitmap, DATE_FORMATTER.format(Date()), TITLE
        )

    }

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification(applicationContext, id,
                applicationContext.getString(R.string.notification_title_saving_image)))
    }

    companion object {
        // Use same notification id as BaseFilter worker to update existing notification. For a real
        // world app you might consider using a different id for each notification.
        private const val NOTIFICATION_ID = 1
        private const val TAG = "SvImageToGalleryWrkr"
        private const val TITLE = "Filtered Image"
        private val DATE_FORMATTER =
            SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())
    }
}