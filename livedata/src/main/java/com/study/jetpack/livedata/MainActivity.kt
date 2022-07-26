package com.study.jetpack.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = MutableLiveData<String>("str")

        data.observe(this) {
            Log.d("JetPack", "livedata onChenged thread ${Thread.currentThread().name} \t new value $it")
        }

        //livedata observe 和 setvalue 必须在主线程 调用。
        // 在子线程中更改livedata 的数据，需要使用postvalue

        thread {
//            data.value = "new str" // LiveData 不能在子线程中调用setvalue
            //
            data.postValue("new str")
        }

    }
}