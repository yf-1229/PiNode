package com.example.pinode.compose.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pinode.data.NodesRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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
                NodeDetailsUiState()
            }
}

data class NodeDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: NodeDetails = NodeDetails()
)