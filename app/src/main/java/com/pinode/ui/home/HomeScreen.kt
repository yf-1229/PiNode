package com.pinode.ui.home

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonGroupDefaults
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
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pinode.BottomNavigationBar
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.item.DateTimeCtrl
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.today_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PiNodeTopAppBar(
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            val items = listOf(
                Icons.Default.Bolt to "Fast Add",
                Icons.Default.Add to "Add"
            )
            val fabVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
            var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
            var checkedProgress by remember { mutableStateOf(0f) }

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
                items.forEachIndexed { i, item ->
                    FloatingActionButtonMenuItem(
                        onClick = {
                            fabMenuExpanded = false
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
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        HomeBody(
            inCompleteNodeList = homeUiState.nodeList.filter {
                (it.deadline?.toLocalDate()?.let { date -> date <= LocalDate.now() } ?: true) && !it.isCompleted && it.status != NodeStatus.NOTTODO
            },
            completedNodeList = homeUiState.nodeList.filter {
                (it.deadline?.toLocalDate()?.let { date -> date <= LocalDate.now() } ?: true) && it.isCompleted
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
            deleteItem = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                }
                viewModel.deleteNode(nodeId)
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

@Composable
fun HomeBody(
    inCompleteNodeList: List<Node>,
    completedNodeList: List<Node>,
    completeItem: (Int) -> Unit,
    editStatus: (Int) -> Unit,
    deleteItem: (Int) -> Unit,
    selectedStatus: (Int, NodeStatus) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (inCompleteNodeList.isEmpty() && completedNodeList.isEmpty()) {
        Text(
            text = stringResource(R.string.no_node_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        )
    } else {
        PiNodeList(
            incompleteNodeList = inCompleteNodeList,
            completedNodeList = completedNodeList,
            completeItem = { node -> completeItem(node.id) },
            editStatus = { node -> editStatus(node.id) },
            deleteItem = { node -> deleteItem(node.id)},
            selectedStatus = { node, status -> selectedStatus(node.id, status) },
            contentPadding = contentPadding,
            modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
        )
    }
}

@Composable
private fun PiNodeList(
    incompleteNodeList: List<Node>,
    completedNodeList: List<Node>,
    completeItem: (Node) -> Unit,
    editStatus: (Node) -> Unit,
    deleteItem: (Node) -> Unit,
    selectedStatus: (Node, NodeStatus) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Box {
        var showDialog by remember { mutableStateOf(false) }
        var selectedNode by remember { mutableStateOf<Node?>(null) }

        val context = LocalContext.current
        val vibrator = context.getSystemService(Vibrator::class.java)

        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            // 未完了タスクを表示
            items(
                items = incompleteNodeList,
                key = { node -> "incomplete_${node.id}" }
            ) { item ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeOut(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    modifier = Modifier.animateItem(
                        fadeInSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        fadeOutSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        placementSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    PiNodeItem(
                        item = item,
                        onItemTap = { node ->
                            selectedNode = node
                            showDialog = true
                        },
                        completeItem = {
                            node -> completeItem(node)
                            vibrator.vibrate(
                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                            ) },
                        editStatus = { node -> editStatus(node) },
                        deleteItem = { node -> deleteItem(node)},
                        showDialog = false
                    )
                }
            }

            if (completedNodeList.isNotEmpty()) {
                item(key = "divider") {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                pathEffect = pathEffect,
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                items(
                    items = completedNodeList,
                    key = { node -> "completed_${node.id}" }
                ) { item ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.animateItem(
                            fadeInSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            fadeOutSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            placementSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        PiNodeItem(
                            item = item,
                            onItemTap = { node ->
                                selectedNode = node
                                showDialog = true
                            },
                            completeItem = { node -> completeItem(node) },
                            editStatus = { node -> editStatus(node) },
                            deleteItem = { node -> deleteItem(node)},
                            showDialog = false
                        )
                    }
                }
            }
        }

        if (showDialog && selectedNode != null) {
            NodeDetailDialog(
                onDismissRequest = { showDialog = false },
                item = selectedNode!!,
                selectedStatus = selectedStatus,
                editStatus = editStatus
            )
        }
    }
}

@Composable
fun PiNodeItem(
    item: Node,
    onItemTap: (Node) -> Unit,
    completeItem: (Node) -> Unit,
    editStatus: (Node) -> Unit,
    deleteItem: (Node) -> Unit,
    showDialog: Boolean,
    modifier: Modifier = Modifier
) {
    // 状態を使用して現在時刻を保持し、更新可能にする
    var currentTime by remember { mutableStateOf(DateTimeCtrl().getNow()) }

    // 一定間隔で時間を更新
    LaunchedEffect(key1 = Unit) {
        while (true) {
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

    val remainingTime = when {
        deadline == null -> {
            "" // 期限なし
        }
        deadline > LocalDateTime.now() && duration <= Duration.ofHours(1) -> {
            // last 1 hour
            duration.toMinutes().toString()
        }
        duration == Duration.ZERO -> {
            // out of deadline
            "0"
        }
        deadline < LocalDateTime.now() -> {
            // out of deadline
            val formatter = DateTimeFormatter.ofPattern("yyyy M/d H:mm")
            "-${formatter.format(item.deadline)}-"
        }
        deadline.year > LocalDateTime.now().year -> {
            // others year
            val formatter = DateTimeFormatter.ofPattern("yyyy M/d H:mm")
            formatter.format(item.deadline)
        }
        deadline.month == LocalDateTime.now().month && deadline.dayOfMonth == LocalDateTime.now().dayOfMonth -> {
            // today
            val formatter = DateTimeFormatter.ofPattern("H:mm")
            formatter.format(item.deadline)
        }
        else -> {
            // this year
            val formatter = DateTimeFormatter.ofPattern("M/d H:mm")
            formatter.format(item.deadline)
        }
    }

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
            ) {
                onItemTap(item)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            // ここでRowを使って左右に分ける
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ステータスインジケーター
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp, top = 6.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            colorResource(item.status.color)
                        )
                )
                // SplitButton
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(40.dp)
                ) {
                    if (!showDialog) {
                        SplitButton(item = item, completeItem = completeItem, editStatus = editStatus, deleteItem = deleteItem)
                    }
                }
            }
            Text( // deadline
                text = remainingTime.toString(),
                color = Color.Gray,
                fontSize = 16.sp,
            )
            Text( // title
                text = item.title,
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle.Default.copy(
                    lineBreak = LineBreak.Heading
                )
            )
            // TODO Sub Todo List
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SplitButton(
    item: Node, completeItem: (Node) -> Unit, editStatus: (Node) -> Unit, deleteItem: (Node) -> Unit,
) {
    var checked by remember { mutableStateOf(false) }
    SplitButtonLayout(
        modifier = Modifier.height(40.dp),
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                onClick = {
                    // 同期的に状態を更新
                    completeItem(item)

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
    DropdownMenu(
        expanded = checked,
        onDismissRequest = { checked = false },
        modifier = Modifier.clip(RoundedCornerShape(6.dp))
    ) {
        DropdownMenuItem(
            text = { Text("Edit") },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null)},
            onClick = {
                editStatus(item)
                checked = false // メニューを閉じる
            },
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
            onClick = {
                deleteItem(item)
                checked = false // メニューを閉じる
            },
        )
    }
}


@Composable
fun NodeDetailDialog(
    onDismissRequest: () -> Unit,
    item: Node,
    selectedStatus: (Node, NodeStatus) -> Unit,
    editStatus: (Node) -> Unit,
) {
    val visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        Box(
            modifier = Modifier
                .animateEnterExit(
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                )
        ) {
            Dialog(onDismissRequest = onDismissRequest) {
                Column {
                    PiNodeItem(
                        item = item,
                        onItemTap = { node -> editStatus(node) },
                        completeItem = {},
                        editStatus = {},
                        deleteItem = {},
                        showDialog = true,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailsButtonGroup(item, selectedStatus)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailsButtonGroup(item: Node, selectedStatus: (Node, NodeStatus) -> Unit) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    val options = listOf(
        NodeStatus.WORKING, NodeStatus.PAUSE, NodeStatus.CARRYOVER, NodeStatus.FAST
    )
    val unCheckedIcons =
        listOf(Icons.Outlined.ArrowUpward, Icons.Outlined.Pause, Icons.Outlined.CalendarMonth, Icons.Outlined.Bolt)
    val checkedIcons =
        listOf(Icons.Filled.ArrowUpward, Icons.Filled.Pause, Icons.Filled.CalendarMonth, Icons.Filled.Bolt)
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row(
        Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        options.forEachIndexed { index, label ->
            ToggleButton(
                checked = selectedIndex == index,
                onCheckedChange = {
                    selectedIndex = index
                    selectedStatus(item, label)
                    vibrator.vibrate(
                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },
                shapes =
                when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
            ) {
                Icon(
                    if (selectedIndex == index) checkedIcons[index] else unCheckedIcons[index],
                    contentDescription = "Localized description",
                )
            }
        }
    }
} // TODO add edit and delete button

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
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
