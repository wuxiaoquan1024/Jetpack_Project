package com.study.jetpack.viewmodel

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

inline fun <reified VM: ViewModel> ComponentActivity.viewModelByActivity(
    noinline factory: (() -> ViewModelProvider.Factory)? = null
) = createViewModelByLazy(VM::class, {this.viewModelStore}, factory)

fun ComponentActivity.getActivityDefaultViewModelFactory() : ViewModelProvider.Factory {
    return SavedStateViewModelFactory(
        application,
        this,
        intent.extras
    )
}

fun<VM: ViewModel> ComponentActivity.createViewModelByLazy(viewModelClass: KClass<VM>,
                                         store: () -> ViewModelStore,
                                         factory: (() -> ViewModelProvider.Factory)?): Lazy<VM> {
    val factoryPromis = factory ?: {
        getActivityDefaultViewModelFactory()
    }
    return ViewModelLazy(viewModelClass, store, factoryPromis)
}



