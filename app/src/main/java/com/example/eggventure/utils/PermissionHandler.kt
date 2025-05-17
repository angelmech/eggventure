package com.example.eggventure.utils

/**
 * Interface for handling permissions in the app.
 */
interface PermissionHandler {
    /**
     * Checks if the required permission is granted. If not, it requests the permission.
     *
     * @param onGranted Callback function that is called with a Boolean indicating whether the permission was granted.
     */
    fun checkAndRequestPermission(onGranted: (Boolean) -> Unit)
}