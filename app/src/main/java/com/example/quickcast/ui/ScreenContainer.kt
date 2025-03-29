package com.example.quickcast.ui

 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.navigationBarsPadding
 import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
 import androidx.compose.foundation.layout.systemBarsPadding
 import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
 import androidx.navigation.compose.rememberNavController
 import com.example.quickcast.Navigation
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

    Scaffold(
        modifier = Modifier.navigationBarsPadding()
            .systemBarsPadding(),
        bottomBar = {BottomBar(
            currentScreen = currentScreen,
            onClick = { destScreen ->
                currentScreen = destScreen
//                navController.navigate(destScreen.name)
            }
        )},
        topBar = {
            Spacer(Modifier.fillMaxWidth().height(16.dp))
        }
    ) { paddingValues ->

        Navigation(paddingValues, navController)

    }
}

@Composable
fun BottomBar(
    currentScreen: BottomNavigationItems,
    onClick : (BottomNavigationItems) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        BottomNavigationItems.entries.forEach {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                        onClick(it)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = null,
//                    modifier = Modifier.size(32.dp),
                    tint = if(currentScreen == it) Color.Black else Color(0xffa1a2a7)
                )
                Text(
                    text = it.name,
                    color = if(currentScreen == it) Color.Black else Color(0xffa1a2a7),
//                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}