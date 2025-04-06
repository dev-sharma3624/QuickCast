package com.example.quickcast.ui.screen_container

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.enum_classes.BottomNavigationItems

@Composable
fun BottomBar(
    currentScreen: BottomNavigationItems,
    onClick : (BottomNavigationItems) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        BottomNavigationItems.entries.forEach {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                        onClick(it)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = null,
//                    modifier = Modifier.size(32.dp),
                    tint = if(currentScreen == it) Color.Black else Color(0xffa1a2a7)
                )
                Text(
                    text = it.name,
                    color = if(currentScreen == it) Color.Black else Color(0xffa1a2a7),
//                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}