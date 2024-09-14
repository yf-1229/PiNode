package com.example.pinode.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawGridWithDots(
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
        for (i in 1..verticalLineCount) {
            for (j in 1..horizontalLineCount) {
                val x = i * gridSpacing
                val y = j * gridSpacing
                drawCircle(
                    color = Color.Red,
                    radius = 8f,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }
        }
    }
}