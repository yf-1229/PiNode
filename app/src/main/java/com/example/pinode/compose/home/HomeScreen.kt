package com.example.pinode.compose.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.R
import com.example.pinode.data.Node
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    // TODO scroll?
    Surface(modifier = Modifier.fillMaxSize()) {


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
    DrawDots(verticalLineCount, horizontalLineCount, )

}

@Composable
private fun DrawGrid(
    verticalLineCount: Int,
    horizontalLineCount: Int,
    strokeWidth: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = Modifier
        .background(Color.LightGray)
        .fillMaxSize()
    ) {
        val gridSpacing = size.width / (verticalLineCount + 1)
        // 縦線を描画
        for (i in 1..verticalLineCount) {
            val x = i * gridSpacing
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(x, 0f),
                end = androidx.compose.ui.geometry.Offset(x, size.height),
                strokeWidth = strokeWidth,
            )
        }

        // 横線を描画
        for (i in 1..horizontalLineCount) {
            val y = i * gridSpacing
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = strokeWidth,
            )
        }
    }
}
