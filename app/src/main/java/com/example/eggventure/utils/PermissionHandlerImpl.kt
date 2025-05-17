package com.example.eggventure.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionHandlerImpl(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>
) : PermissionHandler {

    override fun checkAndRequestPermission(onGranted: (Boolean) -> Unit) {
        val permission = Manifest.permission.ACTIVITY_RECOGNITION

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            onGranted(true)
        } else {
            permissionLauncher.launch(permission)
        }
        //permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
    }
}

