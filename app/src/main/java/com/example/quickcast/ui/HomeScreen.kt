package com.example.quickcast.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickcast.R
import com.example.quickcast.enum_classes.NavigationRailItems

@Composable
fun HomeScreen(paddingValues : PaddingValues){
    var selectedNavigationRailItem by remember { mutableStateOf(NavigationRailItems.SITES) }

    Row(
        modifier = Modifier.padding(paddingValues)
    ) {
        NavigationRail {
            NavigationRailItems.entries.forEach { icon ->
                IconButton(
                    onClick = { item->
                        selectedNavigationRailItem = item
                    },
                    isSelected = icon == selectedNavigationRailItem,
                    item = icon
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 40.dp))
        ) {
            Content()
        }
    }
}

@Composable
fun Content(){
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.sites),
            fontSize = 26.sp
        )
    }
}


@Composable
fun IconButton(
    onClick : (NavigationRailItems) -> Unit,
    isSelected: Boolean,
    item: NavigationRailItems
){
    val shapeAnimation by animateIntAsState(
        if(isSelected) 25 else 50,
        label = "navigation_rail_icon",
        animationSpec = tween(500)
    )

    val colorAnimation by animateColorAsState(
        if(isSelected) Color(0xFF5865F2) else Color.White,
        label = "navigation_rail_icon_color",
        animationSpec = tween(500)
    )

    FloatingActionButton(
        onClick = { onClick(item) },
        elevation = FloatingActionButtonDefaults.elevation(
            0.dp, 0.dp, 0.dp, 0.dp
        ),
        modifier = Modifier.padding(vertical = 8.dp),
        shape = RoundedCornerShape(shapeAnimation),
        containerColor = colorAnimation
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 12.dp),
            tint = if(isSelected) Color.White else Color(0xFF38A967)
        )
    }
}