package com.example.quickcast.ui.screen_container


 import android.app.Activity
 import android.content.Intent
 import android.net.Uri
 import android.provider.Settings
 import android.util.Log
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.compose.animation.AnimatedContent
 import androidx.compose.animation.AnimatedVisibility
 import androidx.compose.animation.core.tween
 import androidx.compose.animation.fadeIn
 import androidx.compose.animation.fadeOut
 import androidx.compose.animation.slideInVertically
 import androidx.compose.animation.slideOutVertically
 import androidx.compose.animation.togetherWith
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.navigationBarsPadding
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.systemBarsPadding
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.automirrored.filled.ArrowForward
 import androidx.compose.material.icons.filled.Add
 import androidx.compose.material3.FabPosition
 import androidx.compose.material3.FloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.Scaffold
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.derivedStateOf
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateListOf
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.vector.ImageVector
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.tooling.preview.Devices
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import androidx.core.app.ActivityCompat
 import androidx.navigation.compose.currentBackStackEntryAsState
 import androidx.navigation.compose.rememberNavController
 import com.example.quickcast.PrimaryNavigation
 import com.example.quickcast.enum_classes.BottomNavigationItems
 import com.example.quickcast.enum_classes.NeededPermissions
 import com.example.quickcast.enum_classes.OtherScreens
 import com.example.quickcast.services.PermissionService
 import com.example.quickcast.ui.dialogs.PermissionAlertDialog
 import com.example.quickcast.ui.theme.QuickCastTheme
 import com.example.quickcast.viewModels.HomeVM
 import org.koin.androidx.compose.koinViewModel

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Composable
fun ScreenContainerPreview(){
    QuickCastTheme {
        ScreenContainer()
    }
}


@Composable
fun ScreenContainer(){

    val navController  = rememberNavController()
    var currentScreen by remember{ mutableStateOf(BottomNavigationItems.Home) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val fabIcon by remember {
        derivedStateOf { when(currentBackStackEntry?.destination?.route){
            BottomNavigationItems.Home.name -> Icons.Default.Add
            OtherScreens.ADD_SITE_SCREEN_FIRST.name -> Icons.AutoMirrored.Filled.ArrowForward
            else -> null
        } }
    }
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

    val homeVM : HomeVM = koinViewModel()

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
            AnimatedVisibility(
                visible = currentBackStackEntry?.destination?.route in listOf(BottomNavigationItems.Home.name,
                    BottomNavigationItems.You.name, BottomNavigationItems.Notifications.name),
                enter = slideInVertically(animationSpec = tween(100)) { it },
                exit = slideOutVertically(animationSpec = tween(100)) { it }
            ) {
                BottomBar(
                    currentScreen = currentScreen,
                    onClick = { destScreen ->
                        if(currentScreen != destScreen){
                            navController.navigate(destScreen.name)
                            currentScreen = destScreen
                        }
                    }
                )
            }
        },

        topBar = {
            Spacer(Modifier.fillMaxWidth().height(16.dp))
        },

        floatingActionButton = {
            if (fabIcon != null){
                Fab(
                    fabIcon = fabIcon!!,
                    onClickFab = {
                        when(fabIcon!!){
                            Icons.Default.Add ->
                                navController.navigate(OtherScreens.ADD_SITE_SCREEN_FIRST.name)

                            Icons.AutoMirrored.Filled.ArrowForward ->
                                if(homeVM.selectedContacts.size > 2){
                                    Log.d("NAMASTE", "size in container : ${homeVM.selectedContacts}")
                                    Log.d("NAMASTE", "size in container : ${homeVM.selectedContacts.size}")
                                    navController.navigate(OtherScreens.ADD_SITE_SCREEN_SECOND.name)
                                }

                            else -> {}
                        }
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

        PrimaryNavigation(paddingValues, navController, homeVM)

    }
}

@Composable
fun Fab(
    fabIcon : ImageVector,
    onClickFab : () -> Unit
){
    Box(
        modifier = Modifier.padding(12.dp)
    ) {
        FloatingActionButton(
            onClick = { onClickFab() },
            content = {
                AnimatedContent(
                    targetState = fabIcon,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(250)))
                            .togetherWith(fadeOut(animationSpec = tween(250)))
                    }
                ) {
                    Icon(
                        imageVector = it,
                        contentDescription = null
                    )
                }

            }
        )
    }
}