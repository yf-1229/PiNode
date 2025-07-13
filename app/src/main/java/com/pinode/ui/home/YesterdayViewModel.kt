package com.pinode.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinode.data.Node
import com.pinode.data.NodeLabel
import com.pinode.data.NodesRepository
import com.pinode.ui.item.NodeDetails
import com.pinode.ui.item.toNode
import com.pinode.ui.item.toNodeDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * ViewModel to retrieve yesterday's nodes.
 */
class YesterdayViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nodesRepository: NodesRepository
) : ViewModel() {

    private val nodeIdKey = "node_id"
    private val nodeId = savedStateHandle.getStateFlow<Int?>(nodeIdKey, null)

    fun updateNodeId(id: Int) {
        savedStateHandle[nodeIdKey] = id
    }

    /**
     * [YesterdayUiState] - Shows nodes that had deadlines yesterday or were completed yesterday
     */
    val yesterdayUiState: StateFlow<YesterdayUiState> =
        nodesRepository.getAllNodesStream().map { nodes ->
            // Filter nodes that had deadlines yesterday or were completed yesterday
            val yesterday = LocalDate.now().minusDays(1)
            val filteredNodes = nodes.filter { node ->
                node.deadline?.let { deadline ->
                    deadline.toLocalDate() == yesterday
                } ?: false
            }
            YesterdayUiState(filteredNodes)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = YesterdayUiState()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> = nodeId
        .filterNotNull()
        .flatMapLatest { id ->
            nodesRepository.getNodeStream(id)
                .filterNotNull()
                .map { node ->
                    UiState(nodeDetails = node.toNodeDetails())
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UiState()
        )

    fun changeNode(label: NodeLabel?) {
        val nodeId = uiState.value.nodeDetails.id
        viewModelScope.launch {
            try {
                val currentNode = nodesRepository.getNodeStream(nodeId).first()

                if (currentNode != null && label == NodeLabel.COMPLETE) {
                    val updatedNode = currentNode.copy(
                        label = label,
                        isCompleted = true
                    )
                    nodesRepository.updateNode(updatedNode)
                } else if (currentNode != null) {
                    val updatedNode = currentNode.copy(
                        label = label,
                        isCompleted = false,
                    )
                    nodesRepository.updateNode(updatedNode)
                } else {
                    Log.e("YesterdayViewModel", "Failed to retrieve currentNode: Node is null")
                }
            } catch (e: Exception) {
                Log.e("YesterdayViewModel", "Failed to update reactions", e)
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

/**
 * Ui State for YesterdayScreen
 */
data class YesterdayUiState(
    val nodeList: List<Node> = listOf()
)