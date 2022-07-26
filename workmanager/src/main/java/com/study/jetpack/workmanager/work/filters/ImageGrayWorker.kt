package com.study.jetpack.workmanager.work.filters

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.Script
import android.renderscript.ScriptC
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.background.ScriptC_grayscale

class ImageGrayWorker(context: Context, params: WorkerParameters) : BaseFilterWork(context, params) {
    override fun applyFitler(input: Bitmap): Bitmap {
        var rsContext: RenderScript? = null
        return try {
            rsContext = RenderScript.create(context)
            val inAllc = Allocation.createFromBitmap(rsContext, input)
            val outAllc = Allocation.createTyped(rsContext, inAllc.type)

            ScriptC_grayscale(rsContext).run {
                _script = this
                _width = input.width.toLong()
                _height = input.height.toLong()
                _in = inAllc
                _out = outAllc
                invoke_filter()
            }

            Bitmap.createBitmap(input.width, input.height, input.config).apply {
                outAllc.copyTo(this)
            }
        } finally {
            rsContext?.finish()
        }
    }
}