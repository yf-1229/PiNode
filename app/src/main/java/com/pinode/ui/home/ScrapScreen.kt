package com.pinode.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.item.toNode
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ScrapDestination : NavigationDestination {
    override val route = "scrap"
    override val titleRes = R.string.scrap_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrapScreen(
    navigateToNodeEdit: (Int) -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
        var showDialog by remember { mutableStateOf(false) }
        HomeBody(
            nodeList = homeUiState.nodeList.filter { it.isCompleted },
            onItemTap = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                    showDialog = true
                }
            },
            selectedItem = { nodeId ->
                viewModel.updateNodeId(nodeId) // update NodeId
            },
            selectedLabel = { label ->
                viewModel.changeNode(label)
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
        if (showDialog) {
            NodeDetailsDialog(
                item = uiState.nodeDetails.toNode(),
                onDismissRequest = { showDialog = false },
                onEdit = {
                    navigateToNodeEdit(uiState.nodeDetails.id)
                    showDialog = false
                },
                onDelete = {
                    coroutineScope.launch {
                        viewModel.deleteNode()
                        showDialog = false
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}