package com.example.pinode.compose.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinode.data.NodesRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NodeEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val nodesRepository: NodesRepository
) : ViewModel(){

    var nodeUiState by mutableStateOf(NodeUiState())
        private set

    private val nodeId: Int = checkNotNull(savedStateHandle[NodeEditDestination.nodeIdArg])

    init {
        viewModelScope.launch {
            nodeUiState = nodesRepository.getNodeStream(nodeId)
                .filterNotNull()
                .first()
                .toNodeUiState(true)
        }
    }

    suspend fun updateNode() {
        if (validateInput(nodeUiState.nodeDetails)) {
            nodesRepository.updateNode(nodeUiState.nodeDetails.toNode())
        }
    }

    fun updateUiState(nodeDetails: NodeDetails) {
        nodeUiState = NodeUiState(
                nodeDetails = nodeDetails,
                isEntryValid = validateInput(nodeDetails)
        )
    }

    private fun validateInput(uiState: NodeDetails = nodeUiState.nodeDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    fun completeNode()
}