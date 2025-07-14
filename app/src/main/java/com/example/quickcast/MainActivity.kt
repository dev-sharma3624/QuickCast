package com.example.quickcast

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

        // checking whether app has been launched by tapping on notification
        val launchBottomSheet = intent?.getBooleanExtra(SmsTypes.SITE_INVITE.name, false)

        val siteInviteString = intent?.getStringExtra("Msg_Body")
        val siteInviteObject = gson.fromJson(siteInviteString, SiteInvite::class.java)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb()),
            navigationBarStyle = SystemBarStyle.light(sideGrillLight.toArgb(), sideGrillLight.toArgb())
        )

        installSplashScreen()

        setContent {

            //viewModel inserted with DI.
            val homeVM : HomeVM = koinViewModel()

            // setting value received from intent to view model
            homeVM.isBottomSheetActive.value = launchBottomSheet!!

            homeVM.siteInviteObject.value = siteInviteObject

            // saving reference of viewModel
            vm = homeVM

            QuickCastTheme {
                ScreenContainer(
                    homeVM = homeVM
                )
            }

        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // checking the value passed by intent when the app is in foreground
        val launchBottomSheet = intent.getBooleanExtra(SmsTypes.SITE_INVITE.name, false)

        val siteInviteString = intent.getStringExtra("Msg_Body")
        val siteInviteObject = gson.fromJson(siteInviteString, SiteInvite::class.java)

        // updating the value in bottom sheet with latest value
        vm?.isBottomSheetActive?.value = launchBottomSheet

        vm?.siteInviteObject?.value = siteInviteObject

    }
}