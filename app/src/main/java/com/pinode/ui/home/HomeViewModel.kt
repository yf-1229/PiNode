package com.pinode.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinode.data.Node
import com.pinode.data.NodeStatus
import com.pinode.data.NodesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    fun completeNode(id: Int) {
        viewModelScope.launch {
            try {
                // 現在のノードを取得
                val currentNode = nodesRepository.getNodeStream(id).first()

                if (currentNode != null) {
                    val updatedNode = currentNode.copy(status = NodeStatus.COMPLETED, isCompleted = true)
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

    fun changeNodeStatus(id: Int, status: NodeStatus) {
        viewModelScope.launch {
            try {
                // 現在のノードを取得
                val currentNode = nodesRepository.getNodeStream(id).first()

                if (currentNode != null) {
                    val updatedNode = currentNode.copy(
                        status = status,
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