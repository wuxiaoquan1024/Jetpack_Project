package com.study.jetpack.workmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.study.jetpack.workmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var requestPermissionCount = 0
    private var hasPermissions = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        if (savedInstanceState != null) {
            requestPermissionCount = savedInstanceState.getInt(KEY_PERMISSION_CUONT)
        }

        requestPermissionIfNecessary()

        with(binding) {
            selectImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_IMAGE_ID)
            }

            selectStockImage.setOnClickListener {
                startActivity(FilterActivity.newInstance(this@MainActivity, StockResource.randomImange()))
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSION_CUONT, requestPermissionCount)
    }

    private fun requestPermissionIfNecessary() {
        hasPermissions = checkUserPermission()
        if (!checkUserPermission()) {
            if (requestPermissionCount < REQUEST_PERMISSION_MAX) {
                ActivityCompat.requestPermissions(this, mPermissions.toTypedArray(), REQUEST_PERMISSION_ID)
                requestPermissionCount++
            } else {
                Snackbar.make(findViewById(R.id.select_constraint_layout), "Go to Settings -> Apps and Notifaction -> workmanager...", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun checkUserPermission() : Boolean {
        var hasPermission = true
        for (permission in mPermissions) {
            hasPermission = hasPermission.and(
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            )
        }
        return hasPermission
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_PERMISSION_ID -> {
                requestPermissionIfNecessary()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) when(requestCode) {
            REQUEST_IMAGE_ID -> {
                handleImageRequestResult(data)
            }
        }
    }

    private fun handleImageRequestResult(data: Intent) {
        val uri = data.clipData!!.getItemAt(0).uri
        if (uri != null) {
            //TODO   启动过滤页面
        }
    }

    companion object {

        const val KEY_PERMISSION_CUONT = "com.study.jetpack.workmanager.request.permission.count"

        const val REQUEST_PERMISSION_ID = 1001

        const val REQUEST_IMAGE_ID = 1002

        const val REQUEST_PERMISSION_MAX = 2

        val mPermissions = mutableListOf<String>(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}