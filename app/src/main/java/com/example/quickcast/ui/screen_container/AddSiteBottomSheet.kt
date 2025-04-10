package com.example.quickcast.ui.screen_container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quickcast.ui.screens.home_sub_screens.AddSiteScreen

@Composable
fun AddSiteBottomSheet(
    bottomSheetController: Boolean,
    callBack : () -> Unit
) {
    AnimatedVisibility(
        visible = bottomSheetController,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500) // Control speed here
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding()
        ) {
            AddSiteScreen{
                callBack()
            }
        }
    }
}