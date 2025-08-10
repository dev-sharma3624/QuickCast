package com.example.quickcast

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quickcast.data_classes.MessageProperties
import com.example.quickcast.enum_classes.BottomNavigationItems
import com.example.quickcast.enum_classes.OtherScreens
import com.example.quickcast.ui.screen_container.ScreenContainer
import com.example.quickcast.ui.screens.HomeScreen
import com.example.quickcast.ui.screens.IndividualSiteScreen
import com.example.quickcast.ui.screens.home_sub_screens.AddSiteScreenFirst
import com.example.quickcast.ui.screens.home_sub_screens.AddSiteScreenSecond
import com.example.quickcast.viewModels.HomeVM
import com.example.quickcast.viewModels.SiteScreenVM
import org.koin.androidx.compose.koinViewModel


/**
 * [PrimaryNavigation] contains the Navigation component of the application.
 *
 * @param paddingValues [PaddingValues] passed on to various screens incoming from [ScreenContainer].
 *
 * @param navHostController [NavHostController] passed from [ScreenContainer]
 *
 * @param homeVM [HomeVM] passed from [ScreenContainer] to function as a shared viewModel for common data that
 * needs to be used by multiple screens.
 * */

@Composable
fun PrimaryNavigation(
    paddingValues: PaddingValues,
    topBar : ((@Composable () -> Unit)?) -> Unit,
    fabData : ((Pair<ImageVector, () -> Unit>)?) -> Unit,
    navHostController: NavHostController,
    homeVM: HomeVM
){

    val siteScreenVM : SiteScreenVM = koinViewModel()

    NavHost(navController = navHostController, startDestination = BottomNavigationItems.Home.name) {

        composable(route = BottomNavigationItems.Home.name){
            HomeScreen(siteScreenVM, paddingValues, navHostController)
        }

        composable(route = BottomNavigationItems.Notifications.name){
            BlankScreen("NOTIFICATION")
        }

        composable(route = BottomNavigationItems.You.name){
            BlankScreen("YOU")
        }

        composable(
            route = OtherScreens.ADD_SITE_SCREEN_FIRST.name,
            enterTransition = if(navHostController.previousBackStackEntry?.destination?.route !=
                OtherScreens.ADD_SITE_SCREEN_FIRST.name){

                { slideInVertically(animationSpec = tween(800)) { it } +
                        fadeIn(animationSpec = tween(800)) }

            }else { null },
            exitTransition = { slideOutVertically(animationSpec = tween(800)) { it } +
                    fadeOut(animationSpec = tween(800)) }
        ) {
            AddSiteScreenFirst(
                callBack = {
                    navHostController.popBackStack()
                    homeVM.clearContactList()
                },
                fabData = {
                    fabData(
                        Pair(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            {
                                if(homeVM.selectedContacts.first().contact != null){
                                    navHostController.navigate(OtherScreens.ADD_SITE_SCREEN_SECOND.name)
                                }
                            }
                        )
                    )
                },
                viewModel = homeVM
            )
        }

        composable(
            route = OtherScreens.ADD_SITE_SCREEN_SECOND.name,
            enterTransition = { slideInHorizontally(animationSpec = tween(800)) { it } +
                    fadeIn(animationSpec = tween(800)) },
            exitTransition = { slideOutHorizontally(animationSpec = tween(800)) { it } +
                    fadeOut(animationSpec = tween(800))}
        ){
            AddSiteScreenSecond(
                fabData = {
                    fabData(
                        Pair(
                            Icons.Default.Check,
                            {
                                if(homeVM.siteName.value != ""){
                                    homeVM.setIsSmsProcessActive()
                                    homeVM.sendMessage()
                                }
                            }
                        )
                    )
                },
                viewModel = homeVM,
                onBackPressed = {
                    navHostController.popBackStack()
                    homeVM.clearSiteName()
                }
            )
        }

        composable(route = OtherScreens.INDIVIDUAL_SITE.name){
            IndividualSiteScreen(
                viewModel = siteScreenVM,
                backNavigationAndCleanUp = {
                    navHostController.popBackStack()
                    topBar(null)
                    siteScreenVM.site = null
                },
                setup = { tB ->
                    topBar{ tB() }
                    fabData(Pair(
                        Icons.Default.Add,
                        {
                            homeVM.showDialog = true
                            if(homeVM.propertyList.isEmpty()){
                                for(i in 0..4){
                                    homeVM.propertyList.add(MessageProperties(i))
                                }
                            }
                        }
                    ))
                }
            )
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
