package com.example.quickcast.enum_classes



/**
 * [NeededPermissions] : enum class containing all the permissions that are needed by
 * the application to function.
 *
 * @param permission contains the string value representing a permission.
 * @param title contains string that will displayed in title of alert dialog if permission is denied.
 * @param description contains description that will displayed in alert dialog if permission is denied but not permanently.
 * @param permanentlyDeniedDescription contains description that will displayed in alert dialog if permission is denied permanently.
 * */

enum class NeededPermissions(
    val permission : String,
    val title : String,
    val description : String,
    val permanentlyDeniedDescription : String
) {

    READ_CONTACTS(
        permission = android.Manifest.permission.READ_CONTACTS,
        title = "Read Contacts Permission",
        description = "This permission is needed to read your phone contacts. Please grant the permission.",
        permanentlyDeniedDescription = "This permission is needed to read your phone contacts. Please grant the permission in app settings."
    ),

    RECEIVE_SMS(
    permission = android.Manifest.permission.RECEIVE_SMS,
    title = "Receive SMS Permission",
    description = "This permission is needed to receive your phone SMS. Please grant the permission.",
    permanentlyDeniedDescription = "This permission is needed to receive your phone SMS. Please grant the permission in app settings."
    ),

    SEND_SMS(
    permission = android.Manifest.permission.SEND_SMS,
    title = "Send SMS Permission",
    description = "This permission is needed to send SMS through your phone. Please grant the permission.",
    permanentlyDeniedDescription = "This permission is needed to send SMS through your phone. Please grant the permission in app settings."
    );


    fun descriptionProvider(isPermanentlyDenied : Boolean) : String{
        return if(isPermanentlyDenied) this.permanentlyDeniedDescription else this.description
    }

}