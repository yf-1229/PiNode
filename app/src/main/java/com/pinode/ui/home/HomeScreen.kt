package com.pinode.ui.home

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
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
import com.pinode.ui.item.toNode
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.today_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navigateToNodeAddFast: () -> Unit,
    navigateToNodeAdd: () -> Unit,
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
                                navigateToNodeAdd()
                            }
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
            nodeList = homeUiState.nodeList,
            onItemTap = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                    showDialog = true
                }
            },
            onItemPress = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                    viewModel.completeNode()
                }
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

@Composable
private fun HomeBody(
    nodeList: List<Node>,
    onItemTap: (Int) -> Unit,
    onItemPress: (Int) -> Unit,
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
                    onItemTap = { onItemTap(it.id)},
                    onItemPress = { onItemPress(it.id) },
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
    onItemTap: (Node) -> Unit?,
    onItemPress: (Node) -> Unit?,
    selectedReactions: (MutableMap<String, Int>?) -> Unit?,
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
                    onTap = { onItemTap(item) },
                    onPress = {onItemPress(item)},
                    selectedReactions = { selectedReactions() }
                )
            }
        }

    }
}


@Composable
private fun PiNodeItem(
    item: Node,
    onTap: () -> Unit?,
    selectedReactions: (MutableMap<String, Int>?) -> Unit?
) {
    // 状態を使用して現在時刻を保持し、更新可能にする
    var currentTime by remember { mutableStateOf(DateTimeCtrl().getNow()) }

    // 絵文字セレクターの表示状態
    var showEmojiSelector by remember { mutableStateOf(false) }

    // アイテムの位置を追跡するための状態
    var itemPosition by remember { mutableStateOf(Offset.Zero) }
    var itemSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

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


    if (!item.isCompleted && item.priority) {
        item.status = NodeStatus.RED
    } else if (!item.isCompleted) {
        item.status = NodeStatus.GREEN
    } else {
        item.status = NodeStatus.GRAY
    } //

    Box {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)

                .onGloballyPositioned { coordinates ->
                    // アイテムの位置とサイズを取得
                    itemPosition = coordinates.positionInRoot()
                    itemSize = coordinates.size.toSize()
            }
                .pointerInput(item) {
                    detectTapGestures(
                        onTap = { onTap() },
                        onLongPress = {
                            // 長押し時に絵文字セレクターを表示
                            showEmojiSelector = true
                        }
                    )
                }
        ) {
            val remainingTime = if (deadline == null) {
                "No Deadline" // 期限なし
            } else if (deadline > LocalDateTime.now() && duration <= Duration.ofHours(2)){
                duration.toMinutes()
            } else if (duration == Duration.ZERO) {
                "JUST!!"
            } else if (deadline < LocalDateTime.now()) {
                val formatter = DateTimeFormatter.ofPattern("M/d H:mm")
                "TimeOUT-${formatter.format(item.deadline)}"
            } else {
                val formatter = DateTimeFormatter.ofPattern("M/d H:mm")
                formatter.format(item.deadline)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(colorResource(item.status.color))
                )

                // アイテムに付いているリアクションを表示（存在する場合）
                val reactions = item.reactions
                if (reactions != null && reactions.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                color = Color(0x33FFFFFF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        reactions.forEach { (emoji, count) ->
                            Text(text = emoji, fontSize = 14.sp)
                            if (count > 1) {
                                Text(
                                    text = count.toString(),
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
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
            HorizontalDivider(thickness = 2.dp)
        }

        // 絵文字セレクター
        AnimatedVisibility(
            visible = showEmojiSelector,
            enter = fadeIn(tween(200)) + expandIn(tween(200), clip = false),
            exit = fadeOut(tween(200)) + shrinkOut(tween(200), clip = false),
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (itemPosition.x + itemSize.width / 2 - 100).roundToInt(),
                        y = (itemPosition.y - 60).roundToInt()
                    )
                }
        ) {
            EmojiSelector(
                onEmojiSelected = { emoji ->
                    // ノードに絵文字リアクションを追加するロジック
                    val currentReactions = item.reactions?.toMutableMap() ?: mutableMapOf()
                    currentReactions[emoji] = (currentReactions[emoji] ?: 0) + 1
                    item.reactions = currentReactions
                    selectedReactions(currentReactions)
                    showEmojiSelector = false
                },
                onDismiss = { showEmojiSelector = false }
            )
        }
    }
}

// 絵文字セレクターのUIコンポーネント
@Composable
fun EmojiSelector(
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val emojis = listOf("❤️", "😂", "😮", "😢", "👍", "🔥")

    Box(
        modifier = Modifier
            .size(200.dp, 50.dp)
            .background(Color(0xE5333333), RoundedCornerShape(24.dp))
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onDismiss() })
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            emojis.forEach { emoji ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onEmojiSelected(emoji)
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}


@Composable
fun NodeDetailsDialog(
    item: Node,
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {

        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            NodeDetails(
                node = item, modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = { onEdit() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_node_title))
            }
            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        onDelete()
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
fun NodeDetails(
    node: Node, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NodeDetailsRow(
                labelResID = R.string.node,
                itemDetail = node.title,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            NodeDetailsRow(
                labelResID = R.string.node_description_req,
                itemDetail = node.description,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
        }

    }
}


@Composable
private fun NodeDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID), fontSize = 20.sp)
        Spacer(modifier = Modifier.weight(2f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold, fontSize = 30.sp)
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


//@Preview
//@Composable
//fun PreviewHomeBody() {
//    val dateTimeCtrl = DateTimeCtrl()
//    PiNodeTheme {
//        HomeBody(listOf(
//            Node(
//                1,
//                NodeStatus.RED,
//                "Test1",
//                "test",
//                label = NodeLabel.PINK,
//                deadline = dateTimeCtrl.getDeadlineByMinutes(5),
//                priority = false,
//                isCompleted = false,
//                isDeleted = false
//            ),
//            Node(
//                2,
//                NodeStatus.RED,
//                "Test2",
//                "test2",
//                label = NodeLabel.PINK,
//                deadline = dateTimeCtrl.getDeadlineByMinutes(selectedMinutes = 50),
//                priority = false,
//                isCompleted = false,
//                isDeleted = false
//            ),
//        ), onItemTap = {}, onItemPress = {})
//    }
//}