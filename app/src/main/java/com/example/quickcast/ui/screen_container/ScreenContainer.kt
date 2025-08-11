package com.example.quickcast.ui.screen_container


 import android.app.Activity
 import android.content.Intent
 import android.net.Uri
 import android.provider.Settings
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
 import androidx.compose.material.icons.filled.Check
 import androidx.compose.material3.BasicAlertDialog
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.FabPosition
 import androidx.compose.material3.FloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.ModalBottomSheet
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.SnackbarHost
 import androidx.compose.material3.SnackbarHostState
 import androidx.compose.material3.rememberModalBottomSheetState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.derivedStateOf
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateListOf
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
 import androidx.compose.runtime.withFrameNanos
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.vector.ImageVector
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import androidx.core.app.ActivityCompat
 import androidx.navigation.compose.currentBackStackEntryAsState
 import androidx.navigation.compose.rememberNavController
 import com.example.quickcast.PrimaryNavigation
 import com.example.quickcast.data_classes.MessageProperties
 import com.example.quickcast.enum_classes.BottomNavigationItems
 import com.example.quickcast.enum_classes.MessagePropertyTypes
 import com.example.quickcast.enum_classes.NeededPermissions
 import com.example.quickcast.enum_classes.OtherScreens
 import com.example.quickcast.services.PermissionService
 import com.example.quickcast.ui.temporary_components.PermissionAlertDialog
 import com.example.quickcast.ui.temporary_components.SiteInviteBottomSheet
 import com.example.quickcast.ui.temporary_components.SnackBarCustom
 import com.example.quickcast.ui.temporary_components.UpdatesMenu
 import com.example.quickcast.viewModels.HomeVM
 import kotlinx.coroutines.delay
 import kotlinx.coroutines.launch

/**
 * [ScreenContainer] parent container of the whole application that contains
 * scaffold layout and calls navigation component.
 * */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(homeVM: HomeVM) {

    val navController  = rememberNavController()
    val context = LocalContext.current

    var fabData by remember { mutableStateOf<Pair<ImageVector, () -> Unit>?>(
        Pair( //pair contains one image vector and one lambda function

            Icons.Default.Add,
            { navController.navigate(OtherScreens.ADD_SITE_SCREEN_FIRST.name) }

        ) // end of Pair
    ) } // end of remember and mutableState

    var topBar by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    //for controlling the color of bottom navigation items based on which is currently selected
    var currentScreen by remember{ mutableStateOf(BottomNavigationItems.Home) }

    //for controlling the icon displayed inside fab and animation for bottom bar to make it visible/invisible
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // required for showing snack bar messages
    val snackBarHostState = remember { SnackbarHostState() }

    // contains entries for permissions that have been denied by the user and require
    // alert dialog to be displayed
    val permissionDialogEntries = remember { mutableStateListOf<NeededPermissions>() }

    //asking for necessary permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions ->
            val permissionService = PermissionService()
            permissions.entries.forEach {
                // if permission is denied
                if(!it.value){
                    // adding permission string to permissionDialogEntries to show custom dialog.
                    permissionDialogEntries.add(permissionService.getNeededPermission(it.key))
                }
            }
        }
    )

    // displaying custom alert dialog if permission(s) is/are denied.
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

    // bottom sheet state to be used by bottom sheet layout that shows site invites
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var showBottomSheet by remember { mutableStateOf(false) }



    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .systemBarsPadding(),

        bottomBar = {
            AnimatedVisibility(
                // displaying/hiding bottom bar based on whether the current page is one
                // of the bottom navigation pages
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
            topBar?.let {
                it()
            }
        },

        floatingActionButton = {
            fabData?.let {
                Fab(
                    fabIcon = it.first,
                    onClickFab = { it.second() }
                )
            }
        },

        floatingActionButtonPosition = FabPosition.End,

        snackbarHost = { SnackbarHost(snackBarHostState){
            SnackBarCustom(it)
        } }

    ) { paddingValues ->

        // asks for permission only once avoiding infinite recompositions.
        LaunchedEffect(Unit) {
            permissionLauncher.launch(PermissionService().getPermissionArray().toTypedArray())
        }

        // showing snack bar if there is valid value of it
        LaunchedEffect(homeVM.snackBarMessage.value) {
            if(homeVM.snackBarMessage.value != ""){
                snackBarHostState.showSnackbar(homeVM.snackBarMessage.value)
            }
        }

        // if the app is in background or is not active and the user taps on notification
        // isBottomSheetActive will be toggled to true value.
        LaunchedEffect(Unit) {
            if(homeVM.isBottomSheetActive.value){
                withFrameNanos { }      // suspends coroutine till app is drawn
                delay(2000)    // adds a little delay for smooth bottom sheet animation
                showBottomSheet = true
                sheetState.show()       // shows bottom sheet
            }
        }

        // when the bottom sheet is dismissed
        LaunchedEffect(homeVM.isBottomSheetActive.value) {
            if(!homeVM.isBottomSheetActive.value){
                launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    showBottomSheet = false
                }
            }else{
                showBottomSheet = true
            }
        }

        PrimaryNavigation(
            paddingValues = paddingValues,
            topBar = {tb ->
                topBar = tb
            },
            fabData = { pair ->
                fabData = pair
            },
            navHostController = navController,
            homeVM = homeVM
        )

        // displaying bottom sheet
        if (showBottomSheet){

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    homeVM.isBottomSheetActive.value = false
                    homeVM.siteInviteObject.value = null
                },
                dragHandle = {}
            ) {
                SiteInviteBottomSheet(
                    siteInvite = homeVM.siteInviteObject,
                    onClickAccept = { homeVM.acceptInvitation() },
                    onClickReject = {}
                )
            }

        }

    }

}



/**
 * [Fab] defines how the fab button will look. It also contains animation to change the icon
 * inside fab based on which screen we're currently at.
 *
 * @param fabIcon icon that is put inside the fab.
 * @param onClickFab function to invoke on click.
 * */

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