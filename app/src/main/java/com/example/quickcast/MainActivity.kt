package com.example.quickcast

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.quickcast.data_classes.SmsFormats.MessageContent
import com.example.quickcast.data_classes.SmsFormats.SiteInvite
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.ui.screen_container.ScreenContainer
import com.example.quickcast.ui.theme.QuickCastTheme
import com.example.quickcast.ui.theme.sideGrillLight
import com.example.quickcast.viewModels.HomeVM
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel

/*putExtra("SITE_NAME", siteInvite.n)
putExtra("SITE_STRENGTH", siteInvite.l?.size ?: 0)*/
class MainActivity : ComponentActivity() {

    // used to store the reference of HomeVM so that it can be used by onNewIntent method
    private var vm : HomeVM? = null
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentObject: Pair<String, MessageContent>? = createMessageContentObjectFromIntent(intent)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb()),
            navigationBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb())
        )

        installSplashScreen()

        setContent {

            //viewModel inserted with DI.
            val homeVM : HomeVM = koinViewModel()

            // saving reference of viewModel
            vm = homeVM

            intentObject?.let {
                setBottomSheetStates(it)
            }

            QuickCastTheme {
                ScreenContainer(
                    homeVM = homeVM
                )
            }

        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val intentObject: Pair<String, MessageContent>? = createMessageContentObjectFromIntent(intent)

        intentObject?.let {
            setBottomSheetStates(it)
        }


    }

    private fun setBottomSheetStates(intentObject : Pair<String, MessageContent>){

        val isSiteInvite = intentObject.second is SiteInvite

        // setting value received from intent to view model
        if (isSiteInvite) {
            vm?.isBottomSheetActive?.value = true
            vm?.siteInviteObject?.value = Pair(intentObject.first, intentObject.second as SiteInvite)
        } else {
            vm?.isBottomSheetActive?.value = false
            vm?.siteInviteObject?.value = null
        }
    }

    private fun createMessageContentObjectFromIntent(intent : Intent?) : Pair<String, MessageContent>?{

        val phoneNumber= intent?.getStringExtra("Phone_Number")
        val msgObject : MessageContent?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            msgObject = intent?.getParcelableExtra("Msg_Object", MessageContent::class.java)

        } else {
            @Suppress("DEPRECATION")
            msgObject =  intent?.getParcelableExtra("Msg_Object") as? MessageContent
        }


        return if(msgObject != null && phoneNumber != null)
                    Pair(phoneNumber, msgObject)
                else
                    null
    }
}