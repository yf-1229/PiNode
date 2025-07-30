package com.pinode.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.data.NodesRepository
import com.pinode.ui.item.NodeDetails
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


    fun changeNode(id: Int, label: NodeStatus?) {
        viewModelScope.launch {
            try {
                // 現在のノードを取得
                val currentNode = nodesRepository.getNodeStream(id).first()

                if (currentNode != null && label == NodeStatus.COMPLETED) {
                    val updatedNode = currentNode.copy(
                        status = label,
                        isCompleted = true
                    )
                    // 更新を保存
                    nodesRepository.updateNode(updatedNode)

                } else if (currentNode != null) {
                    val updatedNode = currentNode.copy(
                        status = label,
                        isCompleted = false,
                    )
                    // 更新を保存
                    nodesRepository.updateNode(updatedNode)

                } else {
                    Log.e("HomeViewModel", "Failed to retrieve currentNode: Node is null")
                }


            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to update reactions", e)
            }
        }
    }
    suspend fun deleteNode(item: Node) {
        nodesRepository.deleteNode(item)
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