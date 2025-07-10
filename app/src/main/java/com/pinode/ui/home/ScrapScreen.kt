package com.pinode.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.R
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination

object ScrapDestination : NavigationDestination {
    override val route = "scrap"
    override val titleRes = R.string.today_title // TODO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    HomeBody(
        nodeList = TODO(),
        onItemTap = TODO(),
        selectedItem = TODO(),
        selectedLabel = TODO(),
        modifier = TODO(),
        contentPadding = TODO()
    )
}