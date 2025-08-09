package com.pinode.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.NodeStatus
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.LocalDate

object FromTomorrowDestination : NavigationDestination {
    override val route = "fromTomorrow"
    override val titleRes = R.string.from_tomorrow_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FromTomorrowScreen(
    navigateToNodeEdit: (Int) -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PiNodeTopAppBar(
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        HomeBody(
            incompleteNodeList = homeUiState.nodeList.filter {
                it.deadline?.toLocalDate()?.let { deadline ->
                    deadline > LocalDate.now()
                } == true && !it.isCompleted && it.status != NodeStatus.NOTTODO
            },
            completedNodeList = homeUiState.nodeList.filter {
                it.deadline?.toLocalDate()?.let { deadline ->
                    deadline > LocalDate.now()
                } == true && it.isCompleted
            },
            completeItem = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                }
                viewModel.completeNode(nodeId)
            },
            editStatus = { nodeId ->
                navigateToNodeEdit(nodeId)
            },
            selectedStatus = { nodeId, status ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                }
                viewModel.changeNodeStatus(nodeId, status)
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}