package com.example.quickcast.ui.screens.home_sub_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.quickcast.ui.screens.IndividualSiteScreen
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.R


/**
 * [SiteScreen] defines how the Site Screen which is a sub-screen of home screen accessed
 * by navigation rail will be displayed.
 * @param onClickSite lambda function used for navigating to [IndividualSiteScreen].
 * */

@Composable
fun SiteScreen(
    onClickSite: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.sites),
            fontSize = 26.sp
        )

        LazyColumn{
            items(3){
                Site(
                    onClickSite = { onClickSite() }
                )
            }
        }
    }
}

/**
 * [Site] defines how each Site element in the Sites list will look.
 * @param onClickSite lambda function used for navigating to [IndividualSiteScreen].
 * */
@Composable
fun Site(
    onClickSite : () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClickSite()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // dp of the group/site
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape)
                .size(50.dp)
                .background(Color.Red)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ){
            // site name
            Text(
                text = "Title String",
                fontWeight = FontWeight.ExtraBold
            )

            // last message
            Text(
                text = "Description String Description String Description String Description String Description StringDescription StringDescription String",
                maxLines = 1
            )
        }

        // green circle depicting whether there is any unread update in any site.
        if(true){
            Box(
                modifier = Modifier
                    .background(Color.Green, CircleShape)
                    .size(5.dp)
            )
        }
    }
}