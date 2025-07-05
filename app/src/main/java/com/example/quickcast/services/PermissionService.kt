package com.example.quickcast.services

import com.example.quickcast.enum_classes.NeededPermissions


/**
 * [PermissionService] : class containing methods that can be used to access data from
 * [NeededPermissions] in desired format.
 * */

class PermissionService {

    /**[getNeededPermission] returns a [NeededPermissions] type object based on the permission string passed. Throws [IllegalArgumentException]*/
    fun getNeededPermission(permission: String): NeededPermissions {
        return NeededPermissions.entries.find { permission == it.permission } ?: throw IllegalArgumentException("Permission $permission is not supported")
    }

    /**[getPermissionArray] returns a list of all permission strings.*/
    fun getPermissionArray(): List<String>{
        val permissionArray = mutableListOf<String>()
        NeededPermissions.entries.forEach {
            permissionArray.add(it.permission)
        }
        return permissionArray
    }
}