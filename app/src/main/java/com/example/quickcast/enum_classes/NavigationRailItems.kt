package com.example.quickcast.enum_classes

import com.example.quickcast.R

/**
 * [NavigationRailItems] : enum class containing all the tabs in navigation rail with their
 * specific icons.
 * */
enum class NavigationRailItems(val icon: Int){
    SITES(R.drawable.site),
    COMPARISON(R.drawable.comparison),
    LIVE_TRACKING(R.drawable.live_tracking)
}