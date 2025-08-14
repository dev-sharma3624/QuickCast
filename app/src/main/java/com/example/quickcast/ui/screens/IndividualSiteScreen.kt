package com.example.quickcast.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.enum_classes.MessagePropertyTypes
import com.example.quickcast.room_db.dto.MessageDTO
import com.example.quickcast.ui.temporary_components.UpdatesMenu
import com.example.quickcast.ui.theme.individualSiteBg
import com.example.quickcast.viewModels.SiteScreenVM

/*
@Preview(showBackground = true)
@Composable
fun IndividualSiteScreenPreview(){
    Column(Modifier.fillMaxSize()) {
        IndividualSiteScreen()
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualSiteScreen(
    viewModel : SiteScreenVM,
    setup : (@Composable () -> Unit) -> Unit,
    backNavigationAndCleanUp : () -> Unit
){

    LaunchedEffect(Unit) {
        setup{
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.site!!.name
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { backNavigationAndCleanUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }

    BackHandler {
        backNavigationAndCleanUp()
    }

    val messageList by viewModel.messageList.collectAsState(emptyList())

    val sentByMeShape : Pair<BubbleShape, Color> = Pair(BubbleShape(true), Color.Green)
    val sentByOthersShape : Pair<BubbleShape, Color> = Pair(BubbleShape(false), Color.DarkGray)

    val shapeDecider : (Boolean) -> Pair<BubbleShape, Color> = { isSentByMe ->
        if(isSentByMe)
            sentByMeShape
        else
            sentByOthersShape
    }

    if(viewModel.showDialog){
        BasicAlertDialog(
            onDismissRequest = { viewModel.showDialog = false}
        ) {
            UpdatesMenu(
                list = viewModel.propertyList,
                nameFieldChange = {id, str ->
                    viewModel.propertyList[id] = MessageProperties(
                        id = id,
                        name = str,
                        value = viewModel.propertyList[id].value,
                        type = viewModel.propertyList[id].type
                    )
                },
                valueFieldChange = {id, value ->
                    viewModel.propertyList[id] = MessageProperties(
                        id = id,
                        name = viewModel.propertyList[id].name,
                        value = value,
                        type = viewModel.propertyList[id].type
                    )
                },
                onClickCount = {id ->
                    viewModel.propertyList[id] = MessageProperties(
                        id = id,
                        name = viewModel.propertyList[id].name,
                        value = viewModel.propertyList[id].value,
                        type = MessagePropertyTypes.COUNT
                    )
                },
                onClickLimit = {id ->
                    viewModel.propertyList[id] = MessageProperties(
                        id = id,
                        name = viewModel.propertyList[id].name,
                        value = viewModel.propertyList[id].value,
                        type = MessagePropertyTypes.LIMIT
                    )
                },
                addMoreFields = {
                    val f = viewModel.propertyList.size
                    for(i in f..f+4){
                        viewModel.propertyList.add(
                            MessageProperties(
                                id = i
                            )
                        )
                    }
                },
                addFields = {
                    viewModel.sendPropertyFieldMsg()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(individualSiteBg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(messageList.isEmpty()){
            Text(
                text = "No updates",
                modifier = Modifier.weight(1f)
            )
        } else{
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
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

        Row {

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
        modifier = Modifier
            .fillMaxWidth()
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