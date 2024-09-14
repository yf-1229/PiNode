package com.example.pinode.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DrawDots(
    verticalLineCount: Int,
    horizontalLineCount: Int,
) {
    Canvas(modifier = Modifier) {
        val gridSpacing = size.width / (verticalLineCount + 1)
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

@Composable
fun NodeItem() {

}