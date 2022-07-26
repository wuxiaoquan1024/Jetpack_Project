package com.study.jetpack.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.work.*
import com.study.jetpack.workmanager.work.CleanUpWorker
import com.study.jetpack.workmanager.work.SaveImageWorker
import com.study.jetpack.workmanager.work.UploadWorker
import com.study.jetpack.workmanager.work.filters.ImageBlurWorker
import com.study.jetpack.workmanager.work.filters.ImageGrayWorker
import com.study.jetpack.workmanager.work.filters.ImageWaterScaleWorker

class ImageOptions(
    private val context: Context,
    private val imageUri: Uri,
    private val blur: Boolean,
    private val gray: Boolean,
    private val water: Boolean,
    private val save: Boolean) {

    private val continuation: WorkContinuation
    val inputData: Data = workDataOf(KEY_IMAGE_URI to imageUri.toString())

    init {
        continuation = WorkManager.getInstance(context)
            .beginUniqueWork(
                FILTER_IMAGE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.from(CleanUpWorker::class.java)
            )
            .thenMayBe<ImageBlurWorker>( blur)
            .thenMayBe<ImageGrayWorker>(gray)
            .thenMayBe<ImageWaterScaleWorker>(water)
            .then(
                if (save) {
                    makeRequest<SaveImageWorker>(tag = TAG_OUTPUT)
                } else {
                    makeRequest<UploadWorker>(tag = TAG_OUTPUT)
                }
            )
    }

    private inline  fun< reified T: ListenableWorker> WorkContinuation.thenMayBe(
                                                        enable: Boolean
    ) : WorkContinuation {
        return if (enable) {
             then(makeRequest<T>())
        } else {
            this
        }
    }


    fun enqueue() {
        continuation.enqueue()
    }

    @SuppressLint("UnsafeOptInUsageError")
    inline fun <reified T: ListenableWorker> makeRequest(
        data: Data = inputData,
        tag: String? = null
    ) = OneTimeWorkRequestBuilder<T>().apply {
        setInputData(data)
        setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        if (tag != null) {
            addTag(tag)
        }
    }.build()
}