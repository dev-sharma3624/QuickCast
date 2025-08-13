package com.example.quickcast.ui.temporary_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quickcast.R
import com.example.quickcast.data_classes.SmsFormats.SiteInvite

@Composable
fun SiteInviteBottomSheet(
    onClickAccept: () -> Unit,
    onClickReject: () -> Unit,
    siteInvite: SiteInvite?
) {

    val internalComponentPadding = Modifier.padding(vertical = 4.dp)

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = internalComponentPadding.clip(shape = CircleShape)
                .background(Color.LightGray, CircleShape)
        ){
            Icon(
                painter = painterResource(R.drawable.group),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
                    .padding(4.dp)
            )
        }

        Text(
            text = siteInvite?.n ?: "Group name",
            fontWeight = FontWeight.Bold,
            modifier = internalComponentPadding
        )

        Text(
            text = "${siteInvite?.l?.size ?: "__"} members",
            color = Color.DarkGray,
            modifier = internalComponentPadding
        )

        Text(
            text = "You have been invited to join this group.",
            modifier = internalComponentPadding
        )

        Spacer(Modifier.padding(vertical = 16.dp))

        OutlinedButton(
            onClick = {onClickAccept()},
            modifier = internalComponentPadding
                .fillMaxWidth(0.8f)
        ) {
            Text("Accept Invite")
        }

        OutlinedButton(
            onClick = {onClickReject()},
            modifier = internalComponentPadding
                .fillMaxWidth(0.8f)
        ) {
            Text("Reject Invite")
        }
    }
}