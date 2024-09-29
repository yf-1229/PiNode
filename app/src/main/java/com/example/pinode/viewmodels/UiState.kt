package com.example.pinode.viewmodels

import com.example.pinode.data.Node

data class UiState(
    val id: Int,
    val currentSelectedNode: Node?,
    val isShowingHomePage: Boolean
    ) {
}
