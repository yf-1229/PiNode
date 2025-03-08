package com.pinode.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.DateTimeConverter
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.item.DateTimeCtrl
import com.pinode.ui.navigation.NavigationDestination
import com.pinode.ui.theme.PiNodeTheme
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNodeEntry: () -> Unit,
    navigateToNodeUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PiNodeTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToNodeEntry,
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
    ) { innerPadding ->
        HomeBody(
            nodeList = homeUiState.nodeList,
            onItemClick = navigateToNodeUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun HomeBody(
    nodeList: List<Node>,
    onItemClick: (Int) -> Unit,
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
            Row {
                PiNodeList(
                    nodeList = nodeList,
                    onItemClick = { onItemClick(it.id) },
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
    onItemClick: (Node) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = nodeList, { it.id }) { item ->
            PiNodeItem(
                item = item,
                modifier = Modifier
                    .clickable { onItemClick(item) }
            )
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
            delay(1000) // 10秒ごとに更新（必要に応じて調整してください）
            currentTime = DateTimeCtrl().getNow()
        }
    }

    val deadline = item.deadline
    val duration = Duration.between(currentTime, deadline)
    val itemColor: Int = item.status.color

    val fontNowSize: TextUnit = when {
        duration.isNegative -> 5.sp
        duration <= Duration.ofMinutes(10) -> 60.sp
        duration <= Duration.ofMinutes(30) -> 20.sp
        else -> 15.sp
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
            text = duration.toString(), // TODO
            color = Color.White,
            fontSize = fontNowSize,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewHomeBody() {
    PiNodeTheme {
        val dateTimeCtrl = DateTimeCtrl()
        val deadline = dateTimeCtrl.getDeadline(5)
        val dateTimeConverter = DateTimeConverter()
        val deadlineStr = dateTimeConverter.localDateTimeToString(deadline)
        val deadlineConverted = dateTimeConverter.stringToLocalDateTime(deadlineStr)
        HomeBody(listOf(
            Node(
                1,
                NodeStatus.RED,
                "Test1",
                "test",
                deadline = dateTimeCtrl.getDeadline(5),
                isCompleted = false,
                isDeleted = false
            ),
            Node(
                2,
                NodeStatus.RED,
                "Test2",
                "test",
                deadline = deadlineConverted,
                isCompleted = false,
                isDeleted = false
            ),
        ), onItemClick = {})
    }
}