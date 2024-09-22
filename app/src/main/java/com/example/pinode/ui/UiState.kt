package com.example.pinode.ui

import androidx.compose.ui.Modifier
import com.example.pinode.data.Node

data class UiState(
    val nodeScreen: Node,
    val currentSelectedNode: Node? = null,
    val isShowingHomePage: Boolean = true,
    ) {
}
