package com.example.quickcast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quickcast.ui.theme.sideGrillLight

@Preview(showBackground = true)
@Composable
fun IndividualSiteScreenPreview(){
    Column(Modifier.fillMaxSize()) {
        IndividualSiteScreen()
    }
}

@Composable
fun IndividualSiteScreen(){

    val sentByMeShape : Pair<BubbleShape, Color> = Pair(BubbleShape(true), Color.Green)
    val sentByOthersShape : Pair<BubbleShape, Color> = Pair(BubbleShape(false), Color.DarkGray)

    val shapeDecider : (Boolean) -> Pair<BubbleShape, Color> = { isSentByMe ->
        if(isSentByMe)
            sentByMeShape
        else
            sentByOthersShape
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(sideGrillLight)
    ) {
        LazyColumn {
            items(count = 5){
                SentUpdate(
                    design = sentByMeShape
                )
            }
        }
    }
}

@Composable
fun SentUpdate(design: Pair<BubbleShape, Color>) {

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(design.first)
                .background(design.second)
        ){
            Text(
                text = "Test message",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}