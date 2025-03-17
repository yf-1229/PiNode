package com.pinode.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pinode.ui.home.HomeDestination
import com.pinode.ui.home.HomeScreen
import com.pinode.ui.item.NodeEditDestination
import com.pinode.ui.item.NodeEditScreen
import com.pinode.ui.item.NodeEntryDestination
import com.pinode.ui.item.NodeEntryScreen

@Composable
fun PiNodeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // Home
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToNodeEntry = { navController.navigate(NodeEntryDestination.route)},
            )
        }
        // NodeEntry
        composable(route = NodeEntryDestination.route) {
            NodeEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable( // NodeEdit
            route = NodeEditDestination.routeWithArgs,
            arguments = listOf(navArgument(NodeEditDestination.nodeIdArg) {
                type = NavType.IntType
            })
        ) {
            NodeEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}