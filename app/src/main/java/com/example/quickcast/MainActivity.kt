package com.example.quickcast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.example.quickcast.ui.ScreenContainer
import com.example.quickcast.ui.theme.QuickCastTheme
import com.example.quickcast.ui.theme.sideGrillLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb()),
            navigationBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb())
        )
        setContent {
            QuickCastTheme {
                ScreenContainer()
            }
        }
    }
}