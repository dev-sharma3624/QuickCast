package com.example.quickcast.enum_classes

import com.example.quickcast.R

/**
 * [BottomNavigationItems] : enum class containing all the tabs in bottom bar with their
 * specific icons.
 * */
enum class BottomNavigationItems(val icon : Int) {
    Home(R.drawable.home),
    Notifications(R.drawable.notifications),
    You(R.drawable.you)
}