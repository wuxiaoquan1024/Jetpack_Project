package com.study.jetpack.workmanager

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.lang.IllegalArgumentException
import java.util.*

class FilterViewModel(app: Application) : ViewModel() {

    val workManager: WorkManager = WorkManager.getInstance(app)

    private val workInfo = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    fun addObserver(owner: LifecycleOwner, observer: Observer<MutableList<WorkInfo>>) {
        workInfo.observe(owner, observer)
    }

    fun enqueue(options: ImageOptions) {
        options.enqueue()
    }

    fun cancel() {
        viewModelScope
        workManager.cancelAllWorkByTag(TAG_OUTPUT)
    }

    class FilterViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
                return FilterViewModel(app) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

    }
}