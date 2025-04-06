package com.example.quickcast.ui.screens.home_sub_screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddSiteScreen(
    callBack : () -> Unit
){
    Text(
        text = "Add site",
        modifier = Modifier.clickable { callBack() }
    )
}