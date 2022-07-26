package com.study.jetpack.workmanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.study.jetpack.workmanager.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {

    private var imageOutputUri: Uri? = null

    private var filterExecuting = false

    private val viewModel: FilterViewModel by viewModels {
        FilterViewModel.FilterViewModelFactory(this.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityFilterBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }.also {
            viewModel.addObserver(this@FilterActivity) {list ->
                if (list.size == 0) return@addObserver else onWorkSateChanged(list[0], it)
            }
            bindView(it)
        }
    }

    private fun bindView(binding: ActivityFilterBinding) {
        val uri = intent.data
        with(binding) {
            Glide.with(root).load(uri).into(image)
            apply.setOnClickListener {
                uri?.let {
                    val options = ImageOptions(this@FilterActivity,
                        it,
                        blur.isChecked,
                        gray.isChecked,
                        water.isChecked,
                        true)
                    viewModel.enqueue(options)
                    filterExecuting = true
                    binding.output.visibility = View.GONE
                }
            }

            output.setOnClickListener {
                imageOutputUri?.let {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = imageOutputUri
                    startActivity(intent)
                }
            }

            cancel.setOnClickListener {
                viewModel.cancel()
            }
        }
    }

    private fun onWorkSateChanged(info: WorkInfo, binding: ActivityFilterBinding) {
        val isFinish = info.state.isFinished
        with(binding) {
            if (!isFinish) {
                blur.isEnabled = false
                gray.isEnabled = false
                water.isEnabled = false
                progress.visibility = View.VISIBLE
                binding.cancel.visibility = View.VISIBLE
            } else {
                blur.isEnabled = true
                blur.isChecked = false
                gray.isEnabled = true
                gray.isChecked = false
                water.isEnabled = true
                water.isChecked = false
                progress.visibility = View.GONE
                binding.cancel.visibility = View.GONE
            }
        }

        info.outputData.getString(KEY_IMAGE_URI)?.let {
            imageOutputUri = Uri.parse(it)
            if (imageOutputUri != null && isFinish && filterExecuting) {
                binding.output.visibility = View.VISIBLE
                Glide.with(this@FilterActivity).load(imageOutputUri).into(findViewById(R.id.image))
                filterExecuting = false
            }
        }
    }

    companion object {

        fun newInstance(context: Context, uri: Uri): Intent {
            val intent = Intent(context, FilterActivity::class.java)
            intent.data = uri
            return intent
        }
    }

}