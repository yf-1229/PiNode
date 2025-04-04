package com.pinode.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.item.DateTimeCtrl
import com.pinode.ui.item.toNode
import com.pinode.ui.navigation.NavigationDestination
import com.pinode.ui.theme.PiNodeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNodeEntry: () -> Unit,
    navigateToNodeEdit: (Int) -> Unit,
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
            FloatingActionButton(
                onClick = navigateToNodeEntry,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.node_entry_title)
                )
            }
        },
        bottomBar = {
            NavigationBarTest()
        }
    ) { innerPadding ->
        var showDialog by remember { mutableStateOf(false) }
        HomeBody(
            nodeList = homeUiState.nodeList,
            onItemClick = { nodeId ->
                coroutineScope.launch {
                    viewModel.updateNodeId(nodeId)
                    showDialog = true
                }
            },
            onItemLongClick = { nodeId ->
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
    onItemClick: (Int) -> Unit,
    onItemLongClick: (Int) -> Unit,
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
                    onItemClick = { onItemClick(it.id)},
                    onItemLongClick = { onItemLongClick(it.id) },
                    contentPadding = contentPadding,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}


@Composable
fun NavigationBarTest() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Songs", "Artists", "Playlists")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Star)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.FavoriteBorder, Icons.Outlined.Star)
    NavigationBar(
        modifier = Modifier,
        tonalElevation = 12.dp,

        ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },

                )
        }
    }

}

@Composable
private fun PiNodeList(
    nodeList: List<Node>,
    onItemClick: (Node) -> Unit?,
    onItemLongClick: (Node) -> Unit?,
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
                    modifier = Modifier
                        .pointerInput(item) {
                            detectTapGestures(
                                onTap = { onItemClick(item) },
                                onLongPress = { onItemLongClick(item) }
                            )
                        }
                )
            }
        }

    }
}


@Composable
private fun PiNodeItem(
    item: Node,
    modifier: Modifier = Modifier
) {
    // 状態を使用して現在時刻を保持し、更新可能にする
    var currentTime by remember { mutableStateOf(DateTimeCtrl().getNow()) }

    // 一定間隔で時間を更新
    LaunchedEffect(key1 = Unit) {
        while(true) {
            delay(1000) // 10秒ごとに更新
            currentTime = DateTimeCtrl().getNow()
        }
    }

    val deadline = item.deadline
    val duration = Duration.between(currentTime, deadline)
    val itemColor: Int = item.status.color


    val fontNowSize: TextUnit = when {
        duration.isNegative -> 120.sp
        duration <= Duration.ofMinutes(5) -> 100.sp
        duration <= Duration.ofMinutes(10) -> 90.sp
        duration <= Duration.ofMinutes(15) -> 80.sp
        duration <= Duration.ofMinutes(30) -> 75.sp
        duration <= Duration.ofMinutes(60) -> 70.sp
        duration <= Duration.ofMinutes(90) -> 65.sp
        duration <= Duration.ofMinutes(120) -> 60.sp
        duration <= Duration.ofMinutes(150) -> 55.sp
        else -> 50.sp
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(colorResource(itemColor))
        )

        Text(
            text = item.title,
            color = Color.White,
            fontSize = fontNowSize,
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle.Default.copy(
                lineBreak = LineBreak.Heading
            )
        )
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


@Preview
@Composable
fun PreviewHomeBody() {
    val dateTimeCtrl = DateTimeCtrl()
    PiNodeTheme {
        HomeBody(listOf(
            Node(
                1,
                NodeStatus.RED,
                "Test1hdsuhcdsjcndjkcdsfdwfew",
                "test",
                deadline = dateTimeCtrl.getDeadline(5),
                isCompleted = false,
                isDeleted = false
            ),
            Node(
                2,
                NodeStatus.RED,
                "Test2",
                "test2",
                deadline = dateTimeCtrl.getDeadline(selectedMinutes = 50),
                isCompleted = false,
                isDeleted = false
            ),
        ), onItemClick = {}, onItemLongClick = {})
    }
}