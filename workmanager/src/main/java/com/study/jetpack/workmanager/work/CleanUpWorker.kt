package com.study.jetpack.workmanager.work

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.study.jetpack.workmanager.OUTPUT_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CleanUpWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val targetDirectory = File(applicationContext.filesDir, OUTPUT_PATH)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                cleanupDirectory()
                Result.success()
            } catch (exception: Exception) {
                Log.e(TAG, "Error cleaning up", exception)
                Result.failure()
            }
        }
    }

    /** Removes pngs from the app's files directory */
    private fun cleanupDirectory() {
        targetDirectory.apply {
            if (exists()) {
                listFiles()?.forEach { file ->
                    if (file.name.endsWith(".png")) {
                        val deleted = file.delete()
                        Log.i(TAG, "Deleted ${file.name} - $deleted")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CleanupWorker"
    }
}