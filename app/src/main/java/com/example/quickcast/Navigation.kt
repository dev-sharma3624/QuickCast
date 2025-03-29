package com.example.quickcast

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickcast.enum_classes.BottomNavigationItems
import com.example.quickcast.ui.HomeScreen

@Composable
fun Navigation(paddingValues: PaddingValues, navController: NavHostController){

    NavHost(navController = navController, startDestination = BottomNavigationItems.Home.name) {

        composable(route = BottomNavigationItems.Home.name){
            HomeScreen(paddingValues, navController)
        }

    }
}