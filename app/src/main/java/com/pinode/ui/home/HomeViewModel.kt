package com.pinode.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinode.data.Node
import com.pinode.data.NodesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(nodesRepository: NodesRepository) : ViewModel() {

    /**
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        nodesRepository.getAllNodesStream().map { nodes ->
            val nodeList1 = nodes.filterIndexed { index, _ -> index % 4 == 0 }
            val nodeList2 = nodes.filterIndexed { index, _ -> (index - 1) % 4 == 0 }
            val nodeList3 = nodes.filterIndexed { index, _ -> (index - 2) % 4 == 0 }
            val nodeList4 = nodes.filterIndexed { index, _ -> (index - 3) % 4 == 0 }
            HomeUiState(nodeList1, nodeList2, nodeList3, nodeList4)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(
    val nodeList1: List<Node> = listOf(),
    val nodeList2: List<Node> = listOf(),
    val nodeList3: List<Node> = listOf(),
    val nodeList4: List<Node> = listOf()
)