package com.example.quickcast.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.data_classes.SmsFormats.CreateTask
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.data_classes.SmsFormats.TaskUpdate
import com.example.quickcast.enum_classes.MessagePropertyTypes
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.ui.temporary_components.NewTaskMenu
import com.example.quickcast.ui.temporary_components.UpdateTaskDialog
import com.example.quickcast.ui.theme.individualSiteBg
import com.example.quickcast.viewModels.SiteScreenVM
import com.google.gson.Gson

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
    backNavigationAndCleanUp : () -> Unit,
    gson : Gson = Gson()
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
    val formatList by viewModel.formatList.collectAsState(emptyList())

    val colorDecider : (SmsTypes) -> Color = { smsType ->
         when(smsType){
            SmsTypes.SITE_INVITE -> Color.Unspecified
            SmsTypes.INVITATION_RESPONSE -> Color.LightGray
            SmsTypes.CREATE_TASK -> Color(0xFF205E22)
            SmsTypes.TASK_UPDATE -> Color(0xFF225F91)
         }
    }

    var taskName by rememberSaveable { mutableStateOf("") }
    var selectedDialog by remember { mutableStateOf("New Task") }

    if(viewModel.showDialog){
        BasicAlertDialog(
            onDismissRequest = { viewModel.showDialog = false}
        ) {

            Column(
                modifier = Modifier.background(Color.White, RoundedCornerShape(5))
                    .height(512.dp)
                    .padding(vertical = 32.dp, horizontal = 8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "New task",
                        fontWeight = if(selectedDialog == "New Task") FontWeight.Bold else null,
                        modifier = Modifier.clickable { selectedDialog = "New Task" }
                    )

                    Text(
                        text = "Update",
                        fontWeight = if(selectedDialog == "Update") FontWeight.Bold else null,
                        modifier = Modifier.clickable { selectedDialog = "Update" }
                    )
                }

                HorizontalDivider(color = Color.Black, modifier = Modifier.padding(vertical = 16.dp))

                if(selectedDialog == "New Task"){
                    NewTaskMenu(
                        list = viewModel.propertyList,
                        nameFieldChange = { id, str ->
                            viewModel.propertyList[id] = MessageProperties(
                                id = id,
                                name = str,
                                value = viewModel.propertyList[id].value,
                                type = viewModel.propertyList[id].type
                            )
                        },
                        valueFieldChange = { id, value ->
                            viewModel.propertyList[id] = MessageProperties(
                                id = id,
                                name = viewModel.propertyList[id].name,
                                value = value,
                                type = viewModel.propertyList[id].type
                            )
                        },
                        onClickCount = { id ->
                            viewModel.propertyList[id] = MessageProperties(
                                id = id,
                                name = viewModel.propertyList[id].name,
                                value = viewModel.propertyList[id].value,
                                type = MessagePropertyTypes.COUNT
                            )
                        },
                        onClickLimit = { id ->
                            viewModel.propertyList[id] = MessageProperties(
                                id = id,
                                name = viewModel.propertyList[id].name,
                                value = viewModel.propertyList[id].value,
                                type = MessagePropertyTypes.LIMIT
                            )
                        },
                        addMoreFields = {
                            val f = viewModel.propertyList.size
                            for (i in f..f + 4) {
                                viewModel.propertyList.add(
                                    MessageProperties(
                                        id = i
                                    )
                                )
                            }
                        },
                        addFields = {
                            viewModel.sendPropertyFieldMsg(taskName)
                        },
                        taskName = taskName,
                        onTaskNameChange = { taskName = it }
                    )
                }else{
                    UpdateTaskDialog(
                        formatList = formatList,
                        onClick = {formatId, taskName, msgProperties, valueList  ->
                            viewModel.sendUpdate(formatId, taskName, msgProperties, valueList)
                        }
                    )
                }

            }

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

            Spacer(Modifier.padding(top = 72.dp))

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
                                    taskName = "",
                                    l = emptyList(),
                                    design = Pair(BubbleShape(messageDTO.sentBy == "SELF"), colorDecider(messageDTO.smsType))
                                )
                            }
                            SmsTypes.INVITATION_RESPONSE -> {
                                InviteResponseMessage(
                                    text = messageDTO.content
                                )
                            }
                            SmsTypes.CREATE_TASK -> {
                                val obj = gson.fromJson(messageDTO.content, CreateTask::class.java)
                                IndividualMessage(
                                    taskName = obj.taskName,
                                    l = obj.l,
                                    design = Pair(BubbleShape(messageDTO.sentBy == "SELF"), colorDecider(messageDTO.smsType))
                                )
                            }

                            SmsTypes.TASK_UPDATE -> {
                                val obj = gson.fromJson(messageDTO.content, TaskUpdate::class.java)
                                IndividualMessage(
                                    taskName = obj.taskName,
                                    l = obj.l,
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
    taskName: String,
    l : List<SendableMessageProperty>,
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = taskName,
                    color = Color.White,
                )
                l.forEach {
                    Row {
                        Text(
                            text = "${it.k} : ",
                            color = Color.White,
                        )
                        Text(
                            text = "${it.v} : ",
                            color = Color.White,
                        )
                        Text(
                            text = it.t.name,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

}