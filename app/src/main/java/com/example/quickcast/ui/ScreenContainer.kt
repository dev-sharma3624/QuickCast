package com.example.quickcast.ui

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
    Scaffold(
        modifier = Modifier.navigationBarsPadding()
            .systemBarsPadding(),
        bottomBar = {BottomBar()},
        topBar = {
            Spacer(Modifier.fillMaxWidth().height(16.dp))
        }
    ) { paddingValues ->

        HomeScreen(paddingValues)

    }
}

@Composable
fun BottomBar(){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        BottomNavigationItems.entries.forEach {
            Column(
                modifier = Modifier
                    .padding(top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = null,
//                    modifier = Modifier.size(32.dp),
                    tint = Color(0xffa1a2a7)
                )
                Text(
                    text = it.name,
                    color = Color(0xffa1a2a7),
//                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}