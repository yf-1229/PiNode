package com.example.pinode.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pinode.compose.home.HomeDestination
import com.example.pinode.compose.home.HomeScreen

@Composable
fun NodeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToNodeEntry = { navController.navigate()} // TODO
                navigateToNodeUpdate = {
                    navController.navigate("${NodeDetailsDestination.route}/${it}")
                }
            )
        }
    }


}