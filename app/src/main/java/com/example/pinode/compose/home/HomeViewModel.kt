package com.example.pinode.compose.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinode.compose.item.NodeDetails
import com.example.pinode.compose.item.NodeUiState
import com.example.pinode.compose.item.toNode
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.data.NodesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(private val nodesRepository: NodesRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [ItemsRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        nodesRepository.getAllNodesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var nodeUiState by mutableStateOf(NodeUiState())
        private set

    fun completeNode(nodeDetails: NodeDetails) {

    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val nodeList: List<Node> = listOf(),
)