package com.study.datastore

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name =  "test")

class MainActivity : AppCompatActivity() {
    lateinit var valueText: TextView
    val activityScope: CoroutineScope = ActivityScope(SupervisorJob() + Dispatchers.IO)
    val random = Random(1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        valueText = findViewById(R.id.data_store_preferences_value)
        findViewById<View>(R.id.read_value).setOnClickListener {
            launch {
                readValueFromPreferences(intPreferencesKey("int_value")) {
                    Log.d("MainActivity", "result value: $it")
                    runOnUiThread {
                        valueText.text = "value: $it"
                    }
                }
            }
        }

        findViewById<View>(R.id.write_value).setOnClickListener {
            launch {
                writeValueToPreferences(intPreferencesKey("int_value"), random.nextInt(1000))
            }
        }
    }

    private suspend fun <T>readValueFromPreferences(key: Preferences.Key<T>, block: (T?) -> Unit) {
        dataStore.data.map {
            val value = it[key]
            Log.d("MainActivity", " value : $value")
            value
        }.collect {
            Log.d("MainActivity", " value resule --- : $it")
            block.invoke(it)
        }
    }

    private suspend fun <T>writeValueToPreferences(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    fun Activity.launch(block: suspend CoroutineScope.() -> Unit) {
        activityScope.launch {
            block.invoke(this)
        }
    }
}