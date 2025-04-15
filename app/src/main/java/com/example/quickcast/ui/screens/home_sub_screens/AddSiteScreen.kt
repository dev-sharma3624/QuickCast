package com.example.quickcast.ui.screens.home_sub_screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.quickcast.services.ContactsService

@Composable
fun AddSiteScreen(
    callBack : () -> Unit
){
    val contacts = ContactsService().getContactsList(LocalContext.current)

    Column {
        Text(
            text = "Add site",
            modifier = Modifier.clickable { callBack() }
        )

        LazyColumn {
            items(contacts){
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(it.name)
                    Text(it.number)
                }
            }
        }
    }
}