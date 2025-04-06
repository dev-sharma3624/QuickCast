package com.example.quickcast.ui.screen_container

 import androidx.compose.animation.AnimatedVisibility
 import androidx.compose.animation.core.tween
 import androidx.compose.animation.slideInVertically
 import androidx.compose.animation.slideOutVertically
 import androidx.compose.foundation.background
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.navigationBarsPadding
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.systemBarsPadding
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.Add
 import androidx.compose.material3.FabPosition
 import androidx.compose.material3.FloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
 import androidx.navigation.compose.rememberNavController
 import com.example.quickcast.BottomBarNavigation
 import com.example.quickcast.enum_classes.BottomNavigationItems
 import com.example.quickcast.ui.theme.QuickCastTheme

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun ScreenContainerPreview(){
    QuickCastTheme {
        ScreenContainer()
    }
}


@Composable
fun ScreenContainer(){

    val navController  = rememberNavController()
    var currentScreen by remember{ mutableStateOf(BottomNavigationItems.Home) }
    var bottomSheetController by remember{ mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.navigationBarsPadding()
            .systemBarsPadding(),

        bottomBar = {
            BottomBar(
            currentScreen = currentScreen,
            onClick = { destScreen ->
                currentScreen = destScreen
                navController.navigate(destScreen.name)
            }
        )
        },

        topBar = {
            Spacer(Modifier.fillMaxWidth().height(16.dp))
        },

        floatingActionButton = {
            Box(
                modifier = Modifier.padding(12.dp)
            ) {
                FloatingActionButton(
                    onClick = {bottomSheetController = true},
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                )
            }
        },

        floatingActionButtonPosition = FabPosition.End

    ) { paddingValues ->

        BottomBarNavigation(paddingValues, navController)

    }

    AddSiteBottomSheet(bottomSheetController){
        bottomSheetController = false
    }
}