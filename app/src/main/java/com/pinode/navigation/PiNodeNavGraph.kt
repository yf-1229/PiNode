package com.pinode.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pinode.compose.home.HomeDestination
import com.pinode.compose.home.HomeScreen
import com.pinode.compose.item.NodeDetailsDestination
import com.pinode.compose.item.NodeDetailsScreen
import com.pinode.compose.item.NodeEditDestination
import com.pinode.compose.item.NodeEditScreen
import com.pinode.compose.item.NodeEntryDestination
import com.pinode.compose.item.NodeEntryScreen

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
                navigateToNodeUpdate = {
                    navController.navigate("${NodeDetailsDestination.route}/${it}")
                },
            )
        }
        // NodeEntry
        composable(route = NodeEntryDestination.route) {
            NodeEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable( // NodeDetails
            route = NodeDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(NodeDetailsDestination.nodeIdArg) {
                type = NavType.IntType
            })
        ) {
            NodeDetailsScreen(
                navigateToEditNode = { navController.navigate("${NodeEditDestination.route}/$it")},
                navigateBack = { navController.navigateUp() }
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