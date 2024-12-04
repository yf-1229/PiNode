package com.pinode.compose.home

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
        nodesRepository.getAllNodesStream().map { HomeUiState(it) }
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
data class HomeUiState(val nodeList: List<Node> = listOf())