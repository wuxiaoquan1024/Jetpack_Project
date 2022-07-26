package com.study.jetpack.workmanager

import android.app.Application
import android.util.Log
import androidx.work.Configuration

class WorkApplication : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(RenameWorkerFactory())
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
    }
}