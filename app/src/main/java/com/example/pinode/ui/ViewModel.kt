package com.example.pinode.ui

import com.example.pinode.data.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private fun initializeUiState() {
        val node: Node = 1 // TODO SQL
        _uiState.value =
            UiState(
                nodeScreen = node,
                currentSelectedNode = null,
                isShowingHomePage = true
            )
    }
}