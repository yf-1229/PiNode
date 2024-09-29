package com.example.pinode.compose.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pinode.data.Node

@Composable
fun NodeListScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val verticalLineCount = 5
        val horizontalLineCount = 20
        val strokeWidth = 3f
        DrawGrid(verticalLineCount, horizontalLineCount, strokeWidth)
        DrawDots(verticalLineCount, horizontalLineCount, Node.Red)

    }
}