package com.example.quickcast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quickcast.enum_classes.BottomNavigationItems
import com.example.quickcast.ui.screens.HomeScreen

@Composable
fun BottomBarNavigation(paddingValues: PaddingValues, navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = BottomNavigationItems.Home.name) {

        composable(route = BottomNavigationItems.Home.name){
            HomeScreen(paddingValues, navHostController)
        }

        composable(route = BottomNavigationItems.Notifications.name){
            BlankScreen("NOTIFICATION")
        }

        composable(route = BottomNavigationItems.You.name){
            BlankScreen("YOU")
        }
    }
}

@Composable
fun BlankScreen(screenName: String) {
    Column(
        Modifier.fillMaxSize()
    ) {
        Text(screenName)
    }
}
