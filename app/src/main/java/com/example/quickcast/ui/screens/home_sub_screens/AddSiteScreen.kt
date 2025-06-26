package com.example.quickcast.ui.screens.home_sub_screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.data_classes.Contact
import com.example.quickcast.data_classes.SelectedContacts
import com.example.quickcast.services.ContactsService
import kotlinx.coroutines.delay

/**
 *[AddSiteScreen] contains the screen content with all the UI components and functionalities
 * to create new groups/sites
 * @param callBack method to close the [AddSiteScreen]
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSiteScreen(
    callBack : () -> Unit
){
    val context = LocalContext.current

    //list of contacts in the phone
    val contacts = ContactsService().getContactsList(context)

    //list of contacts selected for group/site creation
    val selectedContacts = remember { mutableStateListOf(SelectedContacts()) }

    //launched effect aiding selected contacts' row animation
    LaunchedEffect(selectedContacts) {
        //time matches the time of animation
        delay(500)

        //removing entries that have been unselected after selection after exit animation
        if(selectedContacts.isNotEmpty()){
            selectedContacts.removeIf { it.contact != null && !it.isSelected }
        }

        //removing more than one redundant entries
        if(selectedContacts[0] == SelectedContacts() && selectedContacts.size > 1){
            selectedContacts.removeRange(1, selectedContacts.size-1)
        }
    }

    //Closes the bottom sheet when user presses back button
    BackHandler {
        callBack()
    }

    //content parent container
    Column {

        //Top app bar with back navigation, heading and number of contacts
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Select contacts",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${contacts.size} contacts",
                        fontSize = 16.sp
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {callBack()},
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                )
            }
        )

        //Selected contacts horizontal display with animation
        LazyRow {
            items(
                items = selectedContacts
            ){
                AnimatedVisibility(
                    visible = it.isSelected && it.contact != null,
                    enter = scaleIn(
                        initialScale = 0f,
                        animationSpec = tween(500)
                    ),
                    exit = scaleOut(
                        targetScale = 0f,
                        animationSpec = tween(500)
                    )
                ){
                    if (it.contact != null){
                        SelectedContactItem(it){selectedContact ->
                            selectedContacts[selectedContacts.indexOf(selectedContact)].isSelected = false
                            selectedContacts.add(SelectedContacts())
                        }
                    }
                }
            }
        }

        //List of all contacts
        LazyColumn {
            items(contacts){
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    ContactListItem(
                        contact = it,
                        onClick = {selectedContact ->

                            //logic of removing/unselecting an entry
                            if(selectedContacts.contains(selectedContact)){
                                selectedContacts[selectedContacts.indexOf(selectedContact)].isSelected = false
                                selectedContacts.add(SelectedContacts())
                            }
                            //logic of adding/selecting a new entry
                            else{
                                selectedContacts.add(selectedContacts.indexOf(SelectedContacts()), selectedContact)
                            }

                        },
                        isSelected = selectedContacts.contains(SelectedContacts(it, true))
                    )
                }
            }
        }
    }
}

/**
 * [ContactListItem] defines how each individual contact list items will be displayed
 * @param contact [Contact] type object that contains phone number and contact name
 * @param onClick selects/unselects a contact
 * @param isSelected tells whether a contact is currently selected or not
 * */

@Composable
fun ContactListItem(
    contact : Contact,
    onClick : (SelectedContacts) -> Unit,
    isSelected: Boolean
){
    Row(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(SelectedContacts(contact, true)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        DynamicProfilePhoto(
            isSelected = isSelected,
            bgColor = Color.Green,
            tint = Color.Black,
            icon = Icons.Default.Check
        )

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        Column {
            Text(
                text = contact.name
            )
            Text(
                text = contact.number,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
    }
}

/**
 * [SelectedContactItem] defines how the selected contact will display in the lazy row containing selected contacts.
 *
 * @param contact an instance of type [SelectedContacts]
 * @param onClick method to unselect contact from lazy row
 * */

@Composable
fun SelectedContactItem(
    contact: SelectedContacts,
    onClick: (SelectedContacts) -> Unit
) {
    Column(
        modifier = Modifier.padding(8.dp)
            .clickable {
                onClick(contact)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DynamicProfilePhoto(
            isSelected = true,
            bgColor = Color.Gray,
            tint = Color.White,
            icon = Icons.Default.Close
        )

        Text(
            text = contact.contact!!.name
        )
    }
}

/**
 * [DynamicProfilePhoto] defines the current design of profile photo.
 * Used by [SelectedContactItem] where it displays profile with cross-icon
 * and [ContactListItem] where it displays profile with check-icon.
 *
 * @param isSelected tells whether the image is currently selected or not
 * @param bgColor gives the background color to be used
 * @param tint gives the icon color to be used
 * @param icon gives the icon to be used
 * */
@Composable
fun DynamicProfilePhoto(
    isSelected : Boolean,
    bgColor : Color,
    tint : Color,
    icon : ImageVector
){
    Box {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )

        if(isSelected){
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(bgColor, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                        .padding(2.dp)
                )
            }
        }
    }
}