package com.study.jetpack.workmanager.work.filters

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.work.WorkerParameters

class ImageBlurWorker(context: Context, params: WorkerParameters) : BaseFilterWork(context, params) {
    override fun applyFitler(input: Bitmap): Bitmap {
        var scriptContext : RenderScript? = null
        return try {
            scriptContext = RenderScript.create(context)
            val allocInput = Allocation.createFromBitmap(scriptContext, input)
            val allocOut = Allocation.createTyped(scriptContext, allocInput.type)

            val blurScript = ScriptIntrinsicBlur.create(scriptContext, Element.U8_4(scriptContext))
            blurScript.setRadius(25f)
            blurScript.setInput(allocInput)
            blurScript.forEach(allocOut)
            Bitmap.createBitmap(input.width, input.height, input.config).apply {
                allocOut.copyTo(this)
            }
        } finally {
            scriptContext?.finish()
        }

    }
}