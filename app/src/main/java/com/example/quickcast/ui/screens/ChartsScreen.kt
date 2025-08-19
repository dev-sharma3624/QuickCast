package com.example.quickcast.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quickcast.data_classes.MessageGraph
import com.example.quickcast.room_db.entities.Site
import com.example.quickcast.room_db.entities.TaskContentKeys
import com.example.quickcast.ui.theme.individualSiteBg
import com.example.quickcast.viewModels.ChartsVM
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChartsScreen(){

    val viewModel : ChartsVM = koinViewModel()

    val tag = "ChartsScreen"

    Log.d(tag, "composable")


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {

        val initialSetup by viewModel.initialSetup

        Log.d(tag, "$initialSetup")

        if(initialSetup){

            val siteList = viewModel.siteList.collectAsState(emptyList())
            val selectedSite = remember { mutableIntStateOf(0) }
            var isSiteDropDownExpanded by remember { mutableStateOf(false) }

            val formatList = viewModel.formatList.collectAsState(emptyList())
            val selectedFormat = remember { mutableIntStateOf(0) }
            var isFormatDropDownExpanded by remember { mutableStateOf(false) }

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                        .background(
                            individualSiteBg
                        )
                        .padding(4.dp)
                        .clickable {
                            isSiteDropDownExpanded = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if(siteList.value.isNotEmpty()){
                            siteList.value[selectedSite.intValue].name
                        }else { "" },
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isSiteDropDownExpanded,
                    onDismissRequest = { isSiteDropDownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    siteList.value.forEach {
                        DropdownMenuItem(
                            text = { Text(
                                text = it.name
                            ) },
                            onClick = {
                                selectedSite.intValue = siteList.value.indexOf(it)
                                isSiteDropDownExpanded = false
                                viewModel.loadFormats(it.id)
                            },
                        )
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                        .background(
                            individualSiteBg
                        )
                        .padding(4.dp)
                        .clickable {
                            isFormatDropDownExpanded = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    Text(
                        text = if(formatList.value.isNotEmpty()){
                            formatList.value[selectedFormat.intValue].taskName
                        }else { "" }
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isFormatDropDownExpanded,
                    onDismissRequest = { isFormatDropDownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    formatList.value.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.taskName) },
                            onClick = {
                                selectedFormat.intValue = formatList.value.indexOf(it)
                                isFormatDropDownExpanded = false
                                viewModel.loadMessages(it.formatId, it.siteId)
                            }
                        )
                    }
                }
            }

        } else{

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

        }




    }

    /*val siteList = viewModel.siteList.collectAsState(emptyList())
    val selectedSite = remember { mutableIntStateOf(0) }
    var isSiteDropDownExpanded by remember { mutableStateOf(false) }

    var formatList : State<List<TaskContentKeys>>? = remember { null }
    val selectedFormat = remember { mutableStateOf<Int?>(null) }
    var isFormatDropDownExpanded by remember { mutableStateOf(false) }

    var messageList : State<List<MessageGraph>>? = remember { null }


    if (formatList != null) {
        Log.d(tag, "formatList : ${formatList.value}")
    }

    if(siteList.value.isNotEmpty() && formatList == null){
        formatList = viewModel.formatList.collectAsState(emptyList())
        selectedFormat.value = 0
    }


    if((formatList != null && formatList.value.isNotEmpty()) && messageList == null){
        messageList = viewModel.messageList.collectAsState(emptyList())
    }


    LaunchedEffect(siteList.value) {
        Log.d(tag, "siteList CR")
        if(siteList.value.isNotEmpty()){
            viewModel.loadFormats(siteList.value[selectedSite.intValue].id)
        }
    }

    LaunchedEffect(selectedSite.intValue) {
        Log.d(tag, "selectedSite CR")
        if(siteList.value.isNotEmpty()){
            viewModel.loadFormats(siteList.value[selectedSite.intValue].id)
        }
    }

    LaunchedEffect(selectedFormat.value) {
        if(formatList != null && formatList.value.isNotEmpty()){
            viewModel.loadMessages(siteList.value[selectedSite.intValue].id, formatList.value[selectedFormat.value!!].formatId)
        }
    }

    Log.d(tag, "${messageList?.value}")


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        individualSiteBg
                    )
                    .clickable {
                        isSiteDropDownExpanded = true
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if(siteList.value.isNotEmpty()){
                        siteList.value[selectedSite.intValue].name
                    }else { "" }
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = isSiteDropDownExpanded,
                onDismissRequest = { isSiteDropDownExpanded = false },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                siteList.value.forEach {
                    DropdownMenuItem(
                        text = { Text(
                            text = it.name
                        ) },
                        onClick = {
                            selectedSite.intValue = siteList.value.indexOf(it)
                            isSiteDropDownExpanded = false
                        },
                    )
                }
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        individualSiteBg
                    )
                    .clickable {
                        isFormatDropDownExpanded = true
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Text(
                    text = if(formatList != null && formatList.value.isNotEmpty()){
                        formatList.value[selectedFormat.value!!].taskName
                    }else { "" }
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            DropdownMenu(
                expanded = isFormatDropDownExpanded,
                onDismissRequest = { isFormatDropDownExpanded = false },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                formatList?.value?.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.taskName) },
                        onClick = {
                            selectedFormat.value = formatList.value.indexOf(it)
                            isFormatDropDownExpanded = false
                        }
                    )
                }
            }
        }



    }*/


}