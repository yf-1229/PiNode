package com.pinode.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pinode.ui.home.HomeDestination
import com.pinode.ui.home.HomeScreen
import com.pinode.ui.home.ScrapDestination
import com.pinode.ui.home.ScrapScreen
import com.pinode.ui.item.NodeAddDestination
import com.pinode.ui.item.NodeAddFastDestination
import com.pinode.ui.item.NodeAddFastScreen
import com.pinode.ui.item.NodeAddScreen
import com.pinode.ui.item.NodeEditDestination
import com.pinode.ui.item.NodeEditScreen





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PiNodeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "homeScreen",
        modifier = modifier
    ) {
        navigation(startDestination = "homeScreen/${HomeDestination.route}", route = "homeScreen") {
            composable(
                "homeScreen/${HomeDestination.route}",
                enterTransition = { fadeIn(animationSpec = tween(200)) },
                exitTransition = { fadeOut(animationSpec = tween(200)) }
            ) {
                HomeScreen(
                    navigateToNodeAddFast = { navController.navigate(NodeAddFastDestination.route) },
                    navigateToNodeAdd = { navController.navigate(NodeAddDestination.route) },
                    navigateToNodeEdit = { navController.navigate("${NodeEditDestination.route}/$it") },
                    navController = navController
                )
            }

            composable("homeScreen/yesterday") {
                // TODO: YesterdayScreenを実装
            }

            composable(
                "homeScreen/${ScrapDestination.route}",
                enterTransition = { fadeIn(animationSpec = tween(200)) },
                exitTransition = { fadeOut(animationSpec = tween(200)) }
            ) {
                ScrapScreen(
                    navigateToNodeEdit = { navController.navigate("${NodeEditDestination.route}/$it") },
                    navController = navController
                )
            }
        }
        composable(route = NodeAddFastDestination.route) {
            NodeAddFastScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        
        composable(route = NodeAddDestination.route) {
            NodeAddScreen(
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
        // TODO Add Settings
    }
}