package com.example.quickcast.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.example.quickcast.R
import com.example.quickcast.enum_classes.NavigationRailItems

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    Scaffold {
        HomeScreen(it)
    }
}

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
        ) {
            Text(
                text = stringResource(R.string.sites),
                fontSize = 26.sp
            )

            LazyColumn{
                items(3){
                    Site()
                }
            }
        }


        FloatingActionButton(
            onClick = {},
            content = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

}

@Composable
fun Site() {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
            Text(
                text = "Title String",
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Description String Description String Description String Description String Description StringDescription StringDescription String",
                maxLines = 1
            )
        }

        if(true){
            Box(
                modifier = Modifier
                    .background(Color.Green, CircleShape)
                    .size(5.dp)
            )
        }
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