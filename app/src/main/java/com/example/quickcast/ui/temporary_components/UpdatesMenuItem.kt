package com.example.quickcast.ui.temporary_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.enum_classes.MessagePropertyTypes

/**
 * [UpdatesMenu] defines the layout of the Menu where user can set message properties to be sent and recieved.
 *
 * @param list the list of all message properties
 *
 * @param nameFieldChange makes changes to the name field of a property
 *
 * @param valueFieldChange makes change to the value field of a property
 *
 * @param onClickCount makes change to the type field of a property and sets it as [MessagePropertyTypes.COUNT]
 *
 * @param onClickLimit makes change to the type field of a property and sets it as [MessagePropertyTypes.LIMIT]
 *
 * @param addMoreFields adds 5 empty fields to the end of the list
 * */

@Composable
fun UpdatesMenu(
    list: List<MessageProperties>,
    taskName : String,
    onTaskNameChange : (String) -> Unit,
    nameFieldChange : (Int, String) -> Unit,
    valueFieldChange : (Int, Int) -> Unit,
    onClickCount : (Int) -> Unit,
    onClickLimit : (Int) -> Unit,
    addMoreFields : () -> Unit,
    addFields : () -> Unit
) {

    Column(
        modifier = Modifier.background(Color.White, RoundedCornerShape(5))
            .height(512.dp)
            .padding(vertical = 32.dp, horizontal = 8.dp)
    ) {

        Text(
            text = "Task Name",
            modifier = Modifier.padding(horizontal = 4.dp)
        )


        TextField(
            value = taskName,
            onValueChange = { onTaskNameChange(it) },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text("Enter task name")
            }
        )

        // Header Row
        Row {
            Text(
                text = "Name",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.3f),
                fontSize = 18.sp
            )
            Text(
                text = "Value",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.3f),
                fontSize = 18.sp
            )
            Text(
                text = "Type",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.3f),
                fontSize = 18.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // property fields list
            items(list, key = {it.id}){ item ->

                UpdatesMenuItem(
                    value = item,
                    nameFieldChange = { id, newName -> nameFieldChange(id, newName)},
                    valueFieldChange = { id, newValue ->
                        val nV = newValue.replace(Regex("\\D"), "").trimStart('0').ifEmpty { "0" }.toInt()
                        valueFieldChange(id, nV)
                    },
                    onClickCount = { id -> onClickCount(id)},
                    onClickLimit = { id -> onClickLimit(id)}
                )
            }
        }

        // footer
        Text(
            text = "Add more fields?",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp)
                .clickable {addMoreFields()},
            textDecoration = TextDecoration.Underline
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { addFields() }
            ) {
                Text(
                    text = "Add"
                )
            }
        }
    }



}

/**
 * [UpdatesMenuItem] defines the layout af an individual row in the updates menu.
 *
 * @param value contains latest value for name, value and type of an individual [MessageProperties]
 *
 * @param nameFieldChange to change the name parameter
 *
 * @param valueFieldChange to change the value parameter
 *
 * @param onClickCount to change the type parameter to [MessagePropertyTypes.COUNT]
 *
 * @param onClickLimit to change the type parameter to [MessagePropertyTypes.LIMIT]
 * */

@Composable
fun UpdatesMenuItem(
    value : MessageProperties,
    nameFieldChange : (Int, String) -> Unit,
    valueFieldChange : (Int, String) -> Unit,
    onClickCount : (Int) -> Unit,
    onClickLimit : (Int) -> Unit
){

    // controls the dropdown menu
    var isExpanded by remember { mutableStateOf(false) }

    // shows the value of currently selected dropdown item
    var selectedType by remember { mutableStateOf("Count") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(4.dp)
    ) {

        TextField(
            value = value.name,
            onValueChange = { nameFieldChange(value.id, it) },
            modifier = Modifier.weight(0.3f)
                .padding(horizontal = 4.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )


        TextField(
            value = if(value.value == null) "" else value.value.toString(),
            onValueChange = { valueFieldChange(value.id, it) },
            modifier = Modifier.weight(0.3f)
                .padding(horizontal = 4.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10)),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        Button(
            onClick = {
                isExpanded = true
            },
            shape = RectangleShape,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = Color.Black,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = Color.Black
            ),
            modifier = Modifier.weight(0.3f)
                .fillMaxHeight()
        ) {

            Text(
                text = selectedType,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Count") },
                    onClick = {
                        selectedType = "Count"
                        isExpanded = false
                        onClickCount(value.id)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Limit") },
                    onClick = {
                        selectedType = "Limit"
                        isExpanded = false
                        onClickLimit(value.id)
                    }
                )
            }
        }
    }
}