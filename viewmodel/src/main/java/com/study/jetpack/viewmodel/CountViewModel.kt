package com.study.jetpack.viewmodel

import android.os.Looper
import androidx.lifecycle.*

class CountViewModel : ViewModel() {

    var countLiveData = MutableLiveData<Int>(0)
        private set

    fun addObserver(owner: LifecycleOwner, observer: Observer<Int>) {
        countLiveData.observe(owner, observer)
    }

    fun setValue(value: Int) {
        if (Thread.currentThread().name == Looper.getMainLooper().thread.name) {
            countLiveData.value = value
        } else {
            countLiveData.postValue(value)
        }
    }
}