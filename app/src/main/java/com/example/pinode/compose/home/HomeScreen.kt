package com.example.pinode.compose.home

import android.graphics.Matrix
import android.graphics.Outline
import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.R
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.graphics.shapes.toPath
import kotlin.collections.EmptyList.size
import kotlin.collections.EmptySet.size
import kotlin.math.max

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
    val verticalLineCount = 5
    val horizontalLineCount = 20
    val strokeWidth = 3f
    DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth)
    Row(
        modifier = modifier,
    ) {
        NodeList(nodeList[0..10], onItemClick, contentPadding)
    }
}

@Composable
private fun NodeList(
    verticalList: List<Node>,
    onItemClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = verticalList, key = { it.id }) { item ->
            NodeItem(node = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}

@Composable
private fun NodeItem(
    node: Node,
    modifier: Modifier = Modifier
) {
    val title = node.title
    val color = node.status.rgb
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .size(40.dp)
    ) {
        Text(
            title,
            color = Color(color),
            modifier = Modifier.align(Alignment.Center)
        )
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