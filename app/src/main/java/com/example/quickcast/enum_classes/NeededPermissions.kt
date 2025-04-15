package com.example.quickcast.enum_classes



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

    READ_SMS(
    permission = android.Manifest.permission.READ_SMS,
    title = "Read SMS Permission",
    description = "This permission is needed to read your phone SMS. Please grant the permission.",
    permanentlyDeniedDescription = "This permission is needed to read your phone SMS. Please grant the permission in app settings."
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