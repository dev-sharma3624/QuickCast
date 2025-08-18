package com.example.quickcast.ui.temporary_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.quickcast.data_classes.SmsFormats.SendableMessageProperty
import com.example.quickcast.data_classes.SmsFormats.SmsPackage
import com.example.quickcast.enum_classes.MessagePropertyTypes
import com.example.quickcast.room_db.entities.TaskContentKeys
import com.example.quickcast.ui.theme.individualSiteBg
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun UpdateTaskDialog(
    formatList: List<TaskContentKeys>,
    onClick : (Long, String, List<SendableMessageProperty>, List<String>) -> Unit,
    gson : Gson = Gson()
) {

    val selectedFormat = remember { mutableIntStateOf(0) }

    val valueList = remember { mutableStateListOf<String>() }

    val formatObject : MutableState<List<SendableMessageProperty>?> = remember { mutableStateOf(null) }


    LaunchedEffect(selectedFormat.intValue) {

        formatObject.value =   gson.fromJson(
            formatList[selectedFormat.intValue].format,
            object : TypeToken<List<SendableMessageProperty>>() {}.type
        )

        formatObject.value!!.forEach { _ ->
            valueList.add("")
        }
    }

    val isExpanded =  remember { mutableStateOf(false ) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(1f)
                .clickable { isExpanded.value = true }
                .background(individualSiteBg)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = formatList[selectedFormat.intValue].taskName
            )

            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)

        }

        DropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier.width(300.dp)
        ) {
            formatList.forEach {format->

                DropdownMenuItem(
                    text = { Text(
                        text = format.taskName
                    ) },
                    onClick = {
                        selectedFormat.intValue = formatList.indexOf(format)
                        isExpanded.value = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }

    formatObject.value?.let {

        LazyColumn {

            items(it){obj ->

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = obj.k,
                        modifier = Modifier.weight(0.3f)
                    )

                    Text(
                        text = if(obj.t == MessagePropertyTypes.LIMIT) "${obj.t.name}\n(${obj.v})" else obj.t.name,
                        modifier = Modifier.weight(0.3f)
                    )

                    TextField(
                        value = valueList[it.indexOf(obj)],
                        onValueChange = { newValue ->
                            val nV = newValue.replace(Regex("\\D"), "").trimStart('0').ifEmpty { "0" }.toInt()
                            if(obj.t == MessagePropertyTypes.LIMIT && nV > obj.v){
                                valueList[it.indexOf(obj)] = obj.v.toString()
                            }else{
                                valueList[it.indexOf(obj)] = nV.toString()
                            }
                        },
                        modifier = Modifier.weight(0.3f)
                            .padding(horizontal = 4.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(10)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )


                }

            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = {
                onClick(formatList[selectedFormat.intValue].formatId, formatList[selectedFormat.intValue].taskName, formatObject.value!!, valueList)
            }
        ) {
            Text(
                text = "Send"
            )
        }
    }


}