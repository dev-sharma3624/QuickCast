package com.example.quickcast.ui.screens.home_sub_screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quickcast.R
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
    viewModel: HomeVM,
    onBackPressed : () -> Unit
) {

    val isActive by viewModel.isSmsProcessActive

    // controls action of back button
    BackHandler {
        onBackPressed()
    }

    // Parent container
    Column(
        modifier = Modifier.fillMaxSize()
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
                        modifier = Modifier.fillMaxWidth()
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