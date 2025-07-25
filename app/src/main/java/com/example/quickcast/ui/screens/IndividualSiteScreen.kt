package com.example.quickcast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.ui.theme.sideGrillLight
import com.example.quickcast.viewModels.SiteScreenVM
import org.koin.androidx.compose.koinViewModel

/*
@Preview(showBackground = true)
@Composable
fun IndividualSiteScreenPreview(){
    Column(Modifier.fillMaxSize()) {
        IndividualSiteScreen()
    }
}
*/

@Composable
fun IndividualSiteScreen(
    viewModel : SiteScreenVM = koinViewModel()
){

    val messageList by viewModel.messageList.collectAsState()

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
            items(
                items = messageList,
                key = { it.msgId }
            ){
                IndividualMessage(
                    message = it,
                    design = sentByMeShape
                )
            }
        }
    }
}

@Composable
fun MemberUpdateMessage(){
    Row {
        Text(
            text = ""
        )
    }
}

@Composable
fun IndividualMessage(
    message : MessageDTO,
    design: Pair<BubbleShape, Color>
) {

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
                text = message.content,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}