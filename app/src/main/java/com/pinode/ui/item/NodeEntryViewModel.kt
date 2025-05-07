package com.pinode.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.data.NodesRepository
import java.time.LocalDate
import java.time.LocalDateTime


class NodeEntryViewModel(private val nodesRepository: NodesRepository): ViewModel() {
    var nodeUiState by mutableStateOf(NodeUiState())
        private set

    // Update UiState
    fun updateUiState(nodeDetails: NodeDetails) {
        nodeUiState = NodeUiState(
            nodeDetails = nodeDetails,
            isEntryValid = validateInput(nodeDetails),
        )
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
)

data class NodeDetails(
    val id: Int = 0,
    val status: NodeStatus = NodeStatus.GREEN,
    val title: String = "",
    val description: String = "",
    val fontSize: Int = 1,
    val deadline: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false,
    val isDeleted: Boolean = false,
)



fun NodeDetails.toNode(): Node = Node(
    id = id,
    status = status,
    title = title,
    description = description,
    deadline = deadline,
    isCompleted = isCompleted,
    isDeleted = isDeleted
)


fun Node.toNodeUiState(isEntryValid: Boolean = false): NodeUiState = NodeUiState(
    nodeDetails = this.toNodeDetails(),
    isEntryValid = isEntryValid
)

fun Node.toNodeDetails(): NodeDetails = NodeDetails(
    id = id,
    status = status,
    title = title,
    description = description,
    deadline = deadline,
    isCompleted = isCompleted,
    isDeleted = isDeleted
)