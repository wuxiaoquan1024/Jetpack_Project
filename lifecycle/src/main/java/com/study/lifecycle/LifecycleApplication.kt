package com.study.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class LifecycleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object: ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
                Log.d("Lifecycle", "<<<<<<onActivityResumed")
            }

            override fun onActivityPaused(p0: Activity) {
                Log.d("Lifecycle", "<<<<<<onActivityPaused")
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }
        })
    }

}