package com.example.quickcast.ui.screen_container


 import android.app.Activity
 import android.content.Intent
 import android.net.Uri
 import android.provider.Settings
 import android.util.Log
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.navigationBarsPadding
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.systemBarsPadding
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.Add
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.FabPosition
 import androidx.compose.material3.FloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.ModalBottomSheet
 import androidx.compose.material3.ModalBottomSheetProperties
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.rememberModalBottomSheetState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateListOf
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.RectangleShape
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.tooling.preview.Devices
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import androidx.core.app.ActivityCompat
 import androidx.navigation.compose.rememberNavController
 import com.example.quickcast.BottomBarNavigation
 import com.example.quickcast.enum_classes.BottomNavigationItems
 import com.example.quickcast.enum_classes.NeededPermissions
 import com.example.quickcast.services.PermissionService
 import com.example.quickcast.ui.dialogs.PermissionAlertDialog
 import com.example.quickcast.ui.screens.home_sub_screens.AddSiteScreen
 import com.example.quickcast.ui.theme.QuickCastTheme

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun ScreenContainerPreview(){
    QuickCastTheme {
        ScreenContainer()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(){

    val navController  = rememberNavController()
    var currentScreen by remember{ mutableStateOf(BottomNavigationItems.Home) }
    var bottomSheetController by remember{ mutableStateOf(false) }
    val permissionDialogEntries = remember { mutableStateListOf<NeededPermissions>() }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions ->
            val permissionService = PermissionService()
            permissions.entries.forEach {
                if(!it.value){
                    permissionDialogEntries.add(permissionService.getNeededPermission(it.key))
                }
            }
        }
    )

    permissionDialogEntries.forEach {
        PermissionAlertDialog(
            permission = it,
            isDeclinedPermanently = !ActivityCompat.shouldShowRequestPermissionRationale(
                LocalContext.current as Activity,
                it.permission
            ),
            onOkClick = {
                permissionDialogEntries.remove(it)
                permissionLauncher.launch(arrayOf(it.permission))
            },
            onSettingsClick = {
                permissionDialogEntries.remove(it)
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                )
            },
            onDismissRequest = {permissionDialogEntries.remove(it)}
        )
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding()
            .systemBarsPadding(),

        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onClick = { destScreen ->
                    currentScreen = destScreen
                    navController.navigate(destScreen.name)
                }
            )
        },

        topBar = {
            Spacer(Modifier.fillMaxWidth().height(16.dp))
        },

        floatingActionButton = {
            Box(
                modifier = Modifier.padding(12.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        bottomSheetController = true
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                )
            }
        },

        floatingActionButtonPosition = FabPosition.End

    ) { paddingValues ->

        LaunchedEffect(Unit) {
            Log.d("NAMASTE", "recomposition called")
            permissionLauncher.launch(PermissionService().getPermissionArray().toTypedArray())
        }

        BottomBarNavigation(paddingValues, navController)

    }

    AddSiteBottomSheet(bottomSheetController){
        bottomSheetController = false
    }
}