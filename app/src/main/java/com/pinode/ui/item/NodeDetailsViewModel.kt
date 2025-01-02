package com.pinode.ui.item

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinode.data.NodeStatus
import com.pinode.data.NodesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class NodeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val nodesRepository: NodesRepository
) : ViewModel() {
    private val nodeId: Int = checkNotNull(savedStateHandle[NodeDetailsDestination.nodeIdArg])
    val uiState: StateFlow<NodeDetailsUiState> =
        nodesRepository.getNodeStream(nodeId)
            .filterNotNull()
            .map {
                NodeDetailsUiState(nodeDetails = it.toNodeDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NodeDetailsUiState()
            )

    fun completeNode() { // TODO
        viewModelScope.launch {
            val currentItem = uiState.value.nodeDetails.toNode()
            if (!currentItem.isCompleted) {
                currentItem.isCompleted = true
                currentItem.status = NodeStatus.GRAY
                nodesRepository.updateNode(currentItem) // TODO
            }
        }
    }

    suspend fun deleteNode() {
        nodesRepository.deleteNode(uiState.value.nodeDetails.toNode())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class NodeDetailsUiState(
    val nodeDetails: NodeDetails = NodeDetails()
)