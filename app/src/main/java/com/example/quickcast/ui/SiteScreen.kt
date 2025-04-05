package com.example.quickcast.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.R

@Composable
fun SiteScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
        ) {
            Text(
                text = stringResource(R.string.sites),
                fontSize = 26.sp
            )

            LazyColumn{
                items(3){
                    Site()
                }
            }
        }


        FloatingActionButton(
            onClick = {},
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

}

@Composable
fun Site() {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape)
                .size(50.dp)
                .background(Color.Red)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "Title String",
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Description String Description String Description String Description String Description StringDescription StringDescription String",
                maxLines = 1
            )
        }

        if(true){
            Box(
                modifier = Modifier
                    .background(Color.Green, CircleShape)
                    .size(5.dp)
            )
        }
    }
}