package com.example.pinode.compose.home

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pinode.compose.item.NodeUiState
import com.example.pinode.data.NodeStatus

@Composable
fun DrawDots(
    verticalLineCount: Int,
    horizontalLineCount: Int,
    color: String,
    modifier: Modifier = Modifier
) {
    color = color
    Canvas(modifier = Modifier) {
        val gridSpacing = size.width / (verticalLineCount + 1)
        for (i in 1..verticalLineCount) {
            for (j in 1..horizontalLineCount) {
                val x = i * gridSpacing
                val y = j * gridSpacing
                drawCircle(
                    color = Color.Red, // TODO
                    radius = 8f,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }
        }
    }
}