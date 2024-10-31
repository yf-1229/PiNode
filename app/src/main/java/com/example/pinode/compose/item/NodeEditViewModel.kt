package com.example.pinode.compose.item

class NodeEditViewModel {
    suspend fun deleteNode() {
        nodesRepository.deleteNode(uiState.value.itemDetails.toItem())
    }
}