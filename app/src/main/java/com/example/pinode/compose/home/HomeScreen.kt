package com.example.pinode.compose.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.R
import com.example.pinode.compose.item.NodeDetails
import com.example.pinode.compose.item.NodeUiState
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider



object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNodeEntry: () -> Unit,
    navigateToNodeUpdate: (Node) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    // TODO scroll?
    Scaffold(
        modifier = modifier.padding(16.dp),
        topBar = {
            // TODO
        },
        floatingActionButton = {
            // TODO
        },
    ) { innerPadding ->
        val value = nodeDetails.title,
        HomeBody(
            nodeList = homeUiState.nodeList,
            onItemClick = navigateToNodeUpdate,
            onStatusChange = viewModel.completeNode, // TODO
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun HomeBody(
    nodeList: List<Node>,
    onItemClick: (Node) -> Unit,
    onStatusChange: (Node) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val verticalLineCount = 5
    val horizontalLineCount = 20
    val strokeWidth = 3f
    DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth)
    Row(
        modifier = modifier,
    ) {
        PiNodeList(nodeList, onItemClick, onStatusChange, contentPadding)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PiNodeList(
    itemList: List<Node>,
    onItemClick: (Node) -> Unit,
    onStatusChange: (Node) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, { it.id }) { item ->
            PiNodeItem(
                item = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .combinedClickable {
                        onItemClick = { onItemClick(item) }
                        onStatusChange = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            onStatusChange(item)
                        }
                    }
            )
        }
    }
}

@Composable
private fun PiNodeItem(
    item: Node,
    modifier: Modifier = Modifier
) {
    val color = item.status.rgb
    Box(
        modifier = modifier
            .size(20.dp) // 丸のサイズ
            .background(Color.Red) // TODO
            .clip(CircleShape) // 丸い形状にクリップ
    )
}




@Composable
private fun DrawGrid(
    verticalLineCount: Int,
    horizontalLineCount: Int,
    strokeWidth: Float,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = Modifier
        .background(Color.LightGray)
        .fillMaxSize()
    ) {
        val gridSpacing = size.width / (verticalLineCount + 1) // TODO uiStateから今のid数を取得　

        for (i in 1..verticalLineCount) {
            for (j in 1..horizontalLineCount) {
                val x = i * gridSpacing
                val y = j * gridSpacing
                drawLine( // 縦線を描画
                    color = Color.Black,
                    start = androidx.compose.ui.geometry.Offset(x, 0f),
                    end = androidx.compose.ui.geometry.Offset(x, size.height),
                    strokeWidth = strokeWidth,
                )
                drawLine( // 横線を描画
                    color = Color.Black,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}

@Composable
@Preview
fun DrawGridPreview() {
    val verticalLineCount = 5
    val horizontalLineCount = 20
    val strokeWidth = 3f
    val color = NodeStatus.Red
    DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth, color, modifier = Modifier)
}