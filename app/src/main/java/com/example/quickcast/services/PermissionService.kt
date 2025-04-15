package com.example.quickcast.services

import android.util.Log
import com.example.quickcast.enum_classes.NeededPermissions

class PermissionService {

    fun getNeededPermission(permission: String): NeededPermissions {
        return NeededPermissions.entries.find { permission == it.permission } ?: throw IllegalArgumentException("Permission $permission is not supported")
    }

    fun getPermissionArray(): List<String>{
        val permissionArray = mutableListOf<String>()
        NeededPermissions.entries.forEach {
            Log.d("NAMASTE", "forEach loop : ${it.permission}")
            permissionArray.add(it.permission)
        }
        Log.d("NAMASTE", "after for loop : ${permissionArray.first()}")
        return permissionArray
    }
}