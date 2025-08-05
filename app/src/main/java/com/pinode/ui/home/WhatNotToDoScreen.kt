package com.pinode.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object WhatNotToDoDestination : NavigationDestination {
    override val route = "nottodo"
    override val titleRes = R.string.three_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WhatNotToDoScreen(
    navigateToNodeAdd: (Boolean) -> Unit,
    navigateToNodeEdit: (Int) -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var selectedNode by remember { mutableStateOf<Node?>(null) }
    val listState = rememberLazyListState()
    var checkedProgress by remember { mutableStateOf(0f) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PiNodeTopAppBar(
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            val item = Icons.Default.Add to "Add"
            val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
            var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
            BackHandler(fabMenuExpanded) { fabMenuExpanded = false }
            FloatingActionButtonMenu(
                expanded = fabMenuExpanded,
                button = {
                    ToggleFloatingActionButton(
                        modifier = Modifier
                            .semantics {
                                traversalIndex = -1f
                                stateDescription = if (fabMenuExpanded) "Expanded" else "Collapsed"
                                contentDescription = "Toggle menu"
                            }
                            .animateFloatingActionButton(
                                visible = fabVisible || fabMenuExpanded,
                                alignment = Alignment.BottomEnd
                            ),
                        checked = fabMenuExpanded,
                        onCheckedChange = {
                            fabMenuExpanded = !fabMenuExpanded
                            checkedProgress = if (fabMenuExpanded) 1f else 0f
                        }
                    ) {
                        val imageVector = if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                        Icon(
                            painter = rememberVectorPainter(imageVector),
                            contentDescription = null,
                            modifier = Modifier.animateIcon({ checkedProgress })
                        )
                    }
                },
            ) {
                FloatingActionButtonMenuItem(
                    onClick = {
                        fabMenuExpanded = false
                        navigateToNodeAdd(false)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    icon = { Icon(item.first, contentDescription = null) },
                    text = { Text(text = item.second) },
                    modifier = Modifier
                        .padding(
                            end = WindowInsets.safeDrawing
                                .asPaddingValues()
                                .calculateEndPadding(LocalLayoutDirection.current)
                        )
                        .semantics {
                            isTraversalGroup = true
                            customActions =
                                listOf(
                                    CustomAccessibilityAction(
                                        label = "Close menu",
                                        action = {
                                            fabMenuExpanded = false
                                            true
                                        }
                                    )
                                )
                        }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        HomeBody(
            incompleteNodeList = homeUiState.nodeList.filter { !it.isCompleted && it.status == NodeStatus.NOTTODO },
            completedNodeList = homeUiState.nodeList.filter { it.isCompleted && it.status == NodeStatus.NOTTODO },
            completeItem = { nodeId ->
                coroutineScope.launch {
                    viewModel.completeNode(nodeId)
                }
            },
            editStatus = { nodeId -> navigateToNodeEdit(nodeId) },
            selectedStatus = { nodeId, status -> coroutineScope.launch { viewModel.changeNodeStatus(nodeId, status) } },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )

        if (showDialog && selectedNode != null) {
            NodeDetailDialog(
                onDismissRequest = {
                    showDialog = false
                    selectedNode = null
                },
                item = selectedNode!!,
                selectedStatus = { node, status ->
                    coroutineScope.launch {
                        viewModel.changeNodeStatus(node.id, status)
                    }
                },
                editStatus = { node -> navigateToNodeEdit(node.id) },
            )
        }
    }
}