package com.example.quickcast.ui.dialogs

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.enum_classes.NeededPermissions

@Preview(showBackground = true)
@Composable
fun PermissionAlertDialogPreview(){
    Column(Modifier.fillMaxSize()) {
        PermissionAlertDialog(
            NeededPermissions.READ_CONTACTS,
            false,
            {},
            {},
            {}
        )
    }
}

@Composable
fun PermissionAlertDialog(
    permission : NeededPermissions,
    isDeclinedPermanently : Boolean,
    onOkClick : () -> Unit,
    onSettingsClick : () -> Unit,
    onDismissRequest : () -> Unit
){

    Log.d("NAMASTE", "inside alert dialog: ${permission.permission}")

    AlertDialog(
        title = {
            Text(
                text = permission.title,
                modifier = Modifier.fillMaxWidth()
            ) }
        ,
        text = {
            Column {
                Text(
                    text = permission.description,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                HorizontalDivider()
            }
        },
        confirmButton = {
            Text(
                text = if(isDeclinedPermanently) "Go to settings" else "Ok",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if(isDeclinedPermanently){
                            onSettingsClick()
                        }else{
                            Log.d("NAMASTE", "OK")
                            onOkClick()
                        }
                    }
                ,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        },
        onDismissRequest = {
            Log.d("NAMASTE", "Dismiss")
            onDismissRequest()
        },
    )
}