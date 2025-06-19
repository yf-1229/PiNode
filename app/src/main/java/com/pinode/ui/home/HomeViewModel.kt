package com.pinode.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.data.NodesRepository
import com.pinode.ui.item.NodeDetails
import com.pinode.ui.item.toNode
import com.pinode.ui.item.toNodeDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nodesRepository: NodesRepository
) : ViewModel() {

    private val nodeIdKey = "node_id"
    private val nodeId = savedStateHandle.getStateFlow<Int?>(nodeIdKey, null) // null が初期値

    // nodeIdを更新するメソッド
    fun updateNodeId(id: Int) {
        savedStateHandle[nodeIdKey] = id
    }

    /**
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        nodesRepository.getAllNodesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
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

    fun completeNode() {
        viewModelScope.launch {
            val currentItem = uiState.value.nodeDetails.toNode()
            if (!currentItem.isCompleted) {
                currentItem.isCompleted = true
                currentItem.status = NodeStatus.GRAY
                currentItem.emotions = // ホイスト？
                nodesRepository.updateNode(currentItem)
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
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val nodeList: List<Node> = listOf()
)

data class UiState(
    val nodeDetails: NodeDetails = NodeDetails()
)