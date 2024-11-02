package com.example.pinode.compose.item

class NodeEditViewModel savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel(){

    suspend fun deleteNode() {
        nodesRepository.deleteNode(uiState.value.itemDetails.toItem())
    }
}