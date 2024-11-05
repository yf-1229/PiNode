package com.example.pinode.compose.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.data.NodesRepository
import java.text.NumberFormat

class NodeCreateViewModel(private val nodesRepository: NodesRepository): ViewModel() {
    var nodeUiState by mutableStateOf(NodeUiState())
        private set

    // Update UiState
    fun updateUiState(nodeDetails: NodeDetails) {
        nodeUiState = NodeUiState(nodeDetails = nodeDetails, isEntryValid = validateInput(nodeDetails), isCompleted = isComplete, isDeleted = isDeleted)
    }

    suspend fun saveNode() {
        if (validateInput()) {
            nodesRepository.insertNode(nodeUiState.nodeDetails.toNode())
        }
    }

    private fun validateInput(uiState: NodeDetails = nodeUiState.nodeDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }
}


data class NodeUiState(
    val nodeDetails: NodeDetails = NodeDetails(),
    val isEntryValid: Boolean = false,
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false,
    val onDismissRequest: Boolean = false, // TODO ?
)

data class NodeDetails(
    val id: Int = 0,
    val status: NodeStatus = NodeStatus.GRAY,
    val icon: Int = 0,
    val title: String = "",
    val description: String = "",
)

fun NodeDetails.toNode(): Node = Node(
    id = id,
    status = status,
    icon = icon,
    title = title,
    description = description,
)

fun Node.formatedColor(): String {
    return NumberFormat.getCurrencyInstance().format(status)
}

fun Node.toNodeUiState(isEntryValid: Boolean = false): NodeUiState = NodeUiState(
    nodeDetails = this.toNodeDetails(),
    isEntryValid = isEntryValid
)

fun Node.toNodeDetails(): NodeDetails = NodeDetails(
    id = id,
    status = status,
    icon = icon,
    title = title.toString(),
    description = description.toString()
)
