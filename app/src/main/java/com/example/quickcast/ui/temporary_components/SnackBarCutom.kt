package com.example.quickcast.ui.temporary_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay



/**
 * [SnackBarCustom] defines the UI of custom designed snack bar
 *
 * @param snackBarData contains [SnackbarData] specifically snack bar message that needs to displayed.
 * */

@Composable
fun SnackBarCustom(
    snackBarData: SnackbarData
){
    // needed to start entry animation as animation only triggers when starting value is false
    // and then becomes true
    var visible by remember { mutableStateOf(false) }

    // triggering animation
    LaunchedEffect(null) {
        // starting animation
        visible = true

        // time for which snack bar is visible
        delay(3000)

        // starting exit animation
        visible = false
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(animationSpec = tween(1000)) { it } + fadeIn(
            animationSpec = tween(500)
        ),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(32.dp),
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CheckmarkCircle(
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.padding(horizontal = 8.dp))

                Text(
                    text = snackBarData.visuals.message,
                    fontSize = 18.sp
                )
            }
        }
    }
}

