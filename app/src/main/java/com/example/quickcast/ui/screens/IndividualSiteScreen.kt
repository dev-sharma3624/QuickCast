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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.enum_classes.MessagePropertyTypes
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.ui.temporary_components.UpdatesMenu
import com.example.quickcast.ui.theme.individualSiteBg
import com.example.quickcast.viewModels.SiteScreenVM
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    val colorDecider : (SmsTypes) -> Color = { smsType ->
         when(smsType){
            SmsTypes.SITE_INVITE -> Color.Unspecified
            SmsTypes.INVITATION_RESPONSE -> Color.LightGray
            SmsTypes.CREATE_TASK -> Color(0xFF205E22)
        }
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
                ){ messageDTO->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if(messageDTO.sentBy == "SELF") Arrangement.End else Arrangement.Start
                    ){

                        when(messageDTO.smsType){

                            SmsTypes.SITE_INVITE -> {
                                IndividualMessage(
                                    message = "",
                                    design = Pair(BubbleShape(messageDTO.sentBy == "SELF"), colorDecider(messageDTO.smsType))
                                )
                            }
                            SmsTypes.INVITATION_RESPONSE -> {
                                InviteResponseMessage(
                                    text = messageDTO.content
                                )
                            }
                            SmsTypes.CREATE_TASK -> {
                                IndividualMessage(
                                    message = messageDTO.content,
                                    design = Pair(BubbleShape(messageDTO.sentBy == "SELF"), colorDecider(messageDTO.smsType))
                                )
                            }
                        }

                    }
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
fun InviteResponseMessage(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.background(
                Color.LightGray,
                shape = RoundedCornerShape(50)
            )
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun IndividualMessage(
    message: String,
    design: Pair<BubbleShape, Color>
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(design.first)
                .background(design.second)
        ){
            Text(
                text = message,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}