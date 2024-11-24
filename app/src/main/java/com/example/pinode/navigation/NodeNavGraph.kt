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
import com.example.pinode.compose.item.NodeDetailsScreen
import com.example.pinode.compose.item.NodeEditDestination
import com.example.pinode.compose.item.NodeEditScreen
import com.example.pinode.compose.item.NodeEntryDestination
import com.example.pinode.compose.item.NodeEntryScreen

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
                navigateToNodeEntry = { navController.navigate(NodeEntryDestination.route)}, // TODO
                navigateToNodeUpdate = {
                    navController.navigate("${NodeDetailsDestination.route}/${it}")
                },
            )
        }
        // NodeEntry
        composable(route = NodeEntryDestination.route) {
            NodeEntryScreen(
                navigateBack = {},
                onNavigateUp = {}
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