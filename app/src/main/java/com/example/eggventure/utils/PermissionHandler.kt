package com.example.eggventure.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

class PermissionHandler(
    private val context: Context,
    private val permissionLauncher: ActivityResultLauncher<String>
) {

    fun checkAndRequestPermission(onGranted: (Boolean) -> Unit) {
        val permission = Manifest.permission.ACTIVITY_RECOGNITION

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            // Berechtigung bereits erteilt
            onGranted(true)
        } else {
            // Berechtigung noch nicht erteilt, also anfordern
            permissionLauncher.launch(permission)
        }
    }
}

