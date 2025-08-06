package com.example.quickcast.ui.screens.home_sub_screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.R
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.enum_classes.MessagePropertyTypes
import com.example.quickcast.ui.theme.sideGrillLight
import com.example.quickcast.viewModels.HomeVM

/**
 * [AddSiteScreenSecond] contains UI of the screen that appears after the user
 * has selected contacts for site/group creation.
 *
 * @param viewModel viewmodel of type [HomeVM] that contains states and other data with methods required to process them.
 *
 * @param onBackPressed controls navigation when user presses top-app bar navigation icon or system back button.
 * */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddSiteScreenSecond(
    fabData : () -> Unit,
    viewModel: HomeVM,
    onBackPressed : () -> Unit
) {

    // controls property fields animation
    val isUpdateMenuExpanded =  remember { mutableStateOf(false) }

    // animation configuration for updates menu
    val periodicUpdatesAnimation by animateFloatAsState(
        targetValue = if(isUpdateMenuExpanded.value) 90f else 0f
    )

    // controls animation of snack bar after invite is sent
    val isActive by viewModel.isSmsProcessActive

    // number of fields in the updates menu
    var propertyCount by remember { mutableIntStateOf(4) }

    // controls action of back button
    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        fabData()
    }

    // Parent container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        TopAppBar(
            title = {
                Text("New Site")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBackPressed()
                    },
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                )
            }
        )

        AnimatedContent(
            targetState = isActive
        ) {isSmsProcessActive ->

            if(isSmsProcessActive){
                CircularProgressIndicator(
                    color = sideGrillLight
                )
            }else{

                Column {

                    // row containing dp icon button with text field for entering group name
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Dp icon button
                        IconButton(
                            onClick = {},
                            content = {
                                Icon(
                                    painter = painterResource(R.drawable.add_photo),
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.background(
                                color = Color.LightGray,
                                shape = CircleShape
                            )
                        )

                        Spacer(Modifier.padding(horizontal = 16.dp))

                        // text field for entering site/group name
                        OutlinedTextField(
                            value = viewModel.siteName.value,
                            onValueChange = {newValue-> viewModel.siteName.value = newValue},
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Site name",
                                    color = Color.Gray
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Gray
                            )
                        )
                    }

                    HorizontalDivider()

                    // content wrapping capable row that displays all the selected contacts.
                    FlowRow {
                        viewModel.selectedContacts.forEach {

                            //null-check needed for empty contacts added to list for the sake of animation
                            if(it.contact != null){
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Text(
                                        text = it.contact.name,
                                        modifier = Modifier.width(IntrinsicSize.Min),
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

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
    nameFieldChange : (Int, String) -> Unit,
    valueFieldChange : (Int, String) -> Unit,
    onClickCount : (Int) -> Unit,
    onClickLimit : (Int) -> Unit,
    addMoreFields : () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header Row
        item {
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
        }

        // property fields list
        items(list, key = {it.id}){ item ->

            UpdatesMenuItem(
                value = item,
                nameFieldChange = { id, newName -> nameFieldChange(id, newName)},
                valueFieldChange = { id, newValue -> valueFieldChange(id, newValue)},
                onClickCount = { id -> onClickCount(id)},
                onClickLimit = { id -> onClickLimit(id)}
            )
        }


        // footer
        item {
            Text(
                text = "Add more fields?",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clickable {addMoreFields()},
                textDecoration = TextDecoration.Underline
            )
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
            value = value.value.toString(),
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