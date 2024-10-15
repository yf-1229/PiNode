package com.example.pinode.compose.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.R
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider
import androidx.compose.foundation.layout.Column as Column

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
        HomeBody(nodeList = homeUiState.nodeList, onItemClick = navigateToNodeUpdate, modifier = modifier.fillMaxSize(), contentPadding = innerPadding)
    }
}

@Composable
private fun HomeBody(
    nodeList: List<Node>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val verticalLineCount = 5
    val horizontalLineCount = 20
    val strokeWidth = 3f
    val color = NodeStatus.Red

    DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth)

    Row (
        modifier = modifier,
    ) {
        if (nodeList.isEmpty()) {   // TODO うざかったら削除

        } else  {
            NodeList(
                nodeList = nodeList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun NodeList(
    nodeList: List<Node>,
    onItemClick: (Int) -> Unit,
    contentPadding: Modifier,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(items = nodeList, key = {it.id}) { item ->
            // TODO Item
        }
    }
}

@Composable
private fun NodeItem(
    node: Node,
    verticalLineCount: Int,
    horizontalLineCount: Int,
    strokeWidth: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = Modifier) {
        val gridSpacing = size.width / (verticalLineCount + 1)
        Column(modifier = Modifier) {
            drawCircle(
                color = Color.Red, // TODO
                radius = 8f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }
    }
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
        val gridSpacing = size.width / (verticalLineCount + 1)

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