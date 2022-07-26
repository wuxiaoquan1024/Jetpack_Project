package com.study.jetpack.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel

/**
 * ViewModel
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val model: CountViewModel = ViewModelProvider(this).get(CountViewModel::class.java)
        val model: CountViewModel by viewModelByActivity()
        val text = findViewById<TextView>(R.id.count_number)
        model.addObserver(this) {
            text.text = it.toString()
        }

        findViewById<Button>(R.id.increment).setOnClickListener {
            model.countLiveData.value = model.countLiveData.value?.plus(1)
        }

    }
}


