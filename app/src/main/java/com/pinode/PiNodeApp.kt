package com.pinode

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinode.ui.home.HomeDestination
import com.pinode.ui.home.ScrapDestination
import com.pinode.ui.home.WhatNotToDoDestination
import com.pinode.ui.navigation.PiNodeNavHost


@Composable
fun PiNodeApp(navController: NavHostController = rememberNavController()) {
    PiNodeNavHost(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PiNodeTopAppBar(
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }
        }
    )
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    // 定数を remember で最適化
    val navigationItems = remember {
        listOf(
            Triple("Home", "homeScreen/${HomeDestination.route}", Pair(Icons.Filled.Home, Icons.Outlined.Home)),
            Triple("Scrap", "homeScreen/${ScrapDestination.route}", Pair(Icons.Filled.Star, Icons.Outlined.Star)),
            Triple("NotToDo", "homeScreen/${WhatNotToDoDestination.route}", Pair(Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)),
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        navigationItems.forEach { (title, route, icons) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (currentRoute == route) icons.first else icons.second,
                        contentDescription = title
                    )
                },
                label = { Text(title) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo("homeScreen") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}