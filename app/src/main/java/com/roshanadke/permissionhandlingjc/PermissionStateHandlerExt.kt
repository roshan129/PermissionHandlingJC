package com.roshanadke.permissionhandlingjc

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isPermissionPermanentlyDeclined(): Boolean {
    return !hasPermission && !shouldShowRationale
}