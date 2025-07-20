package com.pinode.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.Node
import com.pinode.data.NodeLabel
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.item.DateTimeCtrl
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.today_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navigateToNodeAddFast: () -> Unit,
    navigateToNodeAdd: (Boolean) -> Unit,
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
        floatingActionButton = {
            val listState = rememberLazyListState()
            val items = listOf(
                Icons.Default.Bolt to "Fast Add",
                Icons.Default.Add to "Add"
            )
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
                        onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
                    ) {
                        val imageVector by remember {
                            derivedStateOf {
                                if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
                            }
                        }
                        Icon(
                            painter = rememberVectorPainter(imageVector),
                            contentDescription = null,
                            modifier = Modifier.animateIcon({ checkedProgress })
                        )
                    }
                },
            ) {
                items.forEachIndexed { i, item ->
                    FloatingActionButtonMenuItem(
                        onClick = {
                            if (i == 0) {
                                navigateToNodeAddFast()
                            } else if (i == 1) {
                                navigateToNodeAdd(true)
                            }
                        },
                        containerColor = colorScheme.primary,
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
                                if (i == items.size - 1) {
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
                            }
                        )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        var showDialog by remember { mutableStateOf(false) }
        HomeBody(
            nodeList = homeUiState.nodeList.filter { !it.isCompleted && it.label != NodeLabel.NOTTODO },
            onItemTap = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                    showDialog = true
                }
            },
            selectedItem = { nodeId, label ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                }
                viewModel.changeNode(nodeId, label)
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeBody(
    nodeList: List<Node>,
    onItemTap: (Int) -> Unit,
    selectedItem: (Int, NodeLabel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        if (nodeList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_node_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            Box {
                PiNodeList(
                    nodeList = nodeList,
                    onItemTap = { onItemTap(it.id) },
                    selectedItem = { node, label -> selectedItem(node.id, label) },
                    contentPadding = contentPadding,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}


@Composable
private fun PiNodeList(
    nodeList: List<Node>,
    onItemTap: (Node) -> Unit,
    selectedItem: (Node, NodeLabel) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            items(items = nodeList, { it.id }) { item ->
                PiNodeItem(
                    item = item,
                    selectedItem = { node, label -> selectedItem(node, label) },
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PiNodeItem(
    item: Node,
    selectedItem: (Node, NodeLabel) -> Unit,
) {
    // 状態を使用して現在時刻を保持し、更新可能にする
    var currentTime by remember { mutableStateOf(DateTimeCtrl().getNow()) }

    // 一定間隔で時間を更新
    LaunchedEffect(key1 = Unit) {
        while(true) {
            delay(100)
            currentTime = DateTimeCtrl().getNow()
        }
    }

    val deadline = item.deadline
    val duration = deadline?.let { dateTime ->
        try {
            Duration.between(LocalDateTime.now(), dateTime)
        } catch (e: Exception) {
            Duration.ZERO
        }
    } ?: Duration.ZERO

    val remainingTime = if (deadline == null) {
        "No Deadline" // 期限なし
    } else if (deadline > LocalDateTime.now() && duration <= Duration.ofHours(2)){
        duration.toMinutes()
    } else if (duration == Duration.ZERO) {
        "JUST!!"
    } else if (deadline < LocalDateTime.now()) {
        val formatter = DateTimeFormatter.ofPattern("M/d H:mm")
        "-${formatter.format(item.deadline)}-"
    } else {
        val formatter = DateTimeFormatter.ofPattern("M/d H:mm")
        formatter.format(item.deadline)
    }

    var showDetails by remember { mutableStateOf(false) }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.White),
        modifier = Modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { showDetails = true }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            // ここでRowを使って左右に分ける
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,  // 要素を左右に分ける
                modifier = Modifier.fillMaxWidth()  // 幅いっぱいに広げる
            ) {
                // 左側のステータスインジケーター
                Row {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.label != null) {
                                    colorResource(item.label.color)
                                } else {
                                    Color.Black
                                }
                            )
                    )

                    if (showDetails) {
                        item.label?.let {
                            Text(
                                text = "<-- ${it.text}",
                                fontSize = 12.sp,
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(start = 6.dp)
                            )
                        }
                    }
                }

                // 右側のSplitButton
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(40.dp),
                ) {
                    var checked by remember { mutableStateOf(false) }
                    SplitButtonLayout(
                        modifier = Modifier.height(40.dp),
                        leadingButton = {
                            SplitButtonDefaults.LeadingButton(
                                onClick = {
                                    // 同期的に状態を更新
                                    selectedItem(item, NodeLabel.COMPLETED)
                                },
                                modifier = Modifier.height(40.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = "Localized description",
                                )
                                Spacer(Modifier.size(4.dp))
                                Text("Complete", fontSize = 12.sp)
                            }
                        },
                        trailingButton = {
                            SplitButtonDefaults.TrailingButton(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                },
                                modifier = Modifier
                                    .height(40.dp)
                                    .semantics {
                                        stateDescription =
                                            if (checked) "Expanded" else "Collapsed"
                                        contentDescription = "Toggle Button"
                                    },
                            ) {
                                val rotation: Float by animateFloatAsState(
                                    targetValue = if (checked) 180f else 0f,
                                    label = "Trailing Icon Rotation",
                                )
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .graphicsLayer {
                                            this.rotationZ = rotation
                                        },
                                    contentDescription = "Localized description",
                                )
                            }
                        },
                    )
                    DropdownMenu(expanded = checked, onDismissRequest = { checked = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Working",
                                    fontSize = 12.sp,
                                    color = colorResource(NodeLabel.WORKING.color)
                                )
                            },
                            onClick = {
                                selectedItem(item, NodeLabel.WORKING)
                                checked = false // メニューを閉じる
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.ArrowUpward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Pause",
                                    fontSize = 12.sp,
                                    color = colorResource(NodeLabel.PAUSE.color)
                                )
                            },
                            onClick = {
                                selectedItem(item, NodeLabel.PAUSE)
                                checked = false // メニューを閉じる
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Pause,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Carry over",
                                    fontSize = 12.sp,
                                    color = colorResource(NodeLabel.CARRYOVER.color)
                                )
                            },
                            onClick = {
                                selectedItem(item, NodeLabel.CARRYOVER)
                                checked = false // メニューを閉じる
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Fast",
                                    fontSize = 12.sp,
                                    color = colorResource(NodeLabel.FAST.color)
                                )
                            },
                            onClick = {
                                selectedItem(item, NodeLabel.FAST)
                                checked = false // メニューを閉じる
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.FlashOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                        )
                    }
                }
            }
            Text(
                text = remainingTime.toString(),
                color = Color.Gray,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle.Default.copy(
                    lineBreak = LineBreak.Heading
                )
            )

            if (showDetails) {
                Text(
                    text = item.description,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    style = TextStyle.Default.copy(
                        lineBreak = LineBreak.Heading
                    )
                )
            }
        }
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}