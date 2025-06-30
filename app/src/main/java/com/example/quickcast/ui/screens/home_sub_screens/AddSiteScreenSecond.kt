package com.example.quickcast.ui.screens.home_sub_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.quickcast.BlankScreen

@Composable
fun AddSiteScreenSecond(){
    Column(
        Modifier.background(Color.Cyan)
    ) {
        BlankScreen("hello world")
    }
}