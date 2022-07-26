package com.study.jetpack.workmanager.work.filters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.*
import com.study.jetpack.workmanager.KEY_IMAGE_URI
import com.study.jetpack.workmanager.OUTPUT_PATH
import com.study.jetpack.workmanager.R
import com.study.jetpack.workmanager.work.createNotification
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

abstract class BaseFilterWork(val context: Context, private val parmas: WorkerParameters) : CoroutineWorker(context, parmas) {
    override suspend fun doWork(): Result {
        val imageUri = parmas.inputData.getString(KEY_IMAGE_URI) ?: throw IllegalArgumentException("Invalid input uri")
        return imageUri.let {
            // 1、先将Uri转换Bitmap
            val input = decodeBitmap(context, imageUri)
            // 2、 将Bitmap 进行过滤操作
            val output = applyFitler(input)
            // 3、将过滤完成的Bitmap 缓存本地
            val outputUri = writeBitmapToFile(output)
            // 4、将缓存到本地的Bitmap  地址作为Output 传给下一个
            Result.success(workDataOf(KEY_IMAGE_URI to outputUri))
        }
    }

    abstract fun applyFitler(input: Bitmap) : Bitmap

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(NOTIFACTION_ID, createNotification(
            context,
            id,
            context.resources.getString(R.string.notification_title_filtering_image)))
    }

    private fun writeBitmapToFile(bitmap: Bitmap) : String{
        val name = "filter-output-${UUID.randomUUID()}.png"
        val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ignore: IOException) {
                }
            }
        }
        return Uri.fromFile(outputFile).toString()
    }

    companion object {

        const val ASSET_PREFIX = "file:///android_asset/"

        const val NOTIFACTION_ID = 1001

        private fun decodeBitmap(context: Context, uri: String) : Bitmap {
            if (uri.startsWith(ASSET_PREFIX)) {
                context.resources.assets.open(uri.substring(ASSET_PREFIX.length)).also {
                    return BitmapFactory.decodeStream(it)
                }
            } else {
                context.contentResolver.openInputStream(Uri.parse(uri)).also {
                    return BitmapFactory.decodeStream(it)
                }
            }
        }

    }
}