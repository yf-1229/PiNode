package com.pinode.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.ui.navigation.NavigationDestination

object ScrapDestination : NavigationDestination {
    override val route = "scrap"
    override val titleRes = R.string.scrap_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapScreen(
    navigateToNodeEdit: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            incompleteNodeList = homeUiState.nodeList.filter { !it.isCompleted && it.status != NodeStatus.NOTTODO },
            completedNodeList = homeUiState.nodeList.filter { it.isCompleted },
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
}