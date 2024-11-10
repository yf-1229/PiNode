package com.example.pinode.compose.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinode.data.NodesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Thread.State

class NodeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val nodesRepository: NodesRepository
) : ViewModel() {
    private val nodeId: Int = checkNotNull(savedStateHandle[NodeDetailsDestination.nodeIdArg])

    val uiState: StateFlow<NodeDetailsUiState> =
        nodesRepository.getAllNodesStream(nodeId)
            .filterNotNull()
            .map {
                NodeDetailsUiState(nodeDetails = it.toNodeDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NodeDetailsUiState()
            )

    suspend fun deleteNode() {
        nodesRepository.deleteItem(uiState.value.nodeDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class NodeDetailsUiState(
    val nodeDetails: NodeDetails = NodeDetails()
)