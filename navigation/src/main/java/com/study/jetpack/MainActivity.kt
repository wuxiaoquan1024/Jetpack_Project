package com.study.jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private var homeIsEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, dest, args ->
            Log.d("Tag", "dest name ${dest.displayName}")
            if (dest.id == R.id.home && !homeIsEnable) {
                homeIsEnable = true
            }
        }
    }

    override fun onBackPressed() {
        if (!homeIsEnable) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "首页不能直接退出应用", Toast.LENGTH_LONG).show()
        }

    }
}