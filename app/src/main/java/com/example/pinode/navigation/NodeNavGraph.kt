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
import com.example.pinode.compose.item.NodeDetails
import com.example.pinode.compose.item.NodeDetailsDestination
import com.example.pinode.compose.item.NodeEditDestination

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
                navigateToNodeEntry = { navController.navigate()}, // TODO
                navigateToNodeUpdate = {
                    navController.navigate("${NodeDetailsDestination.route}/${it}")
                },
            )
        }
        composable(
            route = NodeDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(NodeDetailsDestination.nodeIdArg) {
                type = NavType.IntType
            }) {

        }
    }


}