package com.pinode.data

import kotlinx.coroutines.flow.Flow


interface NodesRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllNodesStream(): Flow<List<Node>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getNodeStream(id: Int): Flow<Node?>

    /**
     * Insert item in the data source
     */
    suspend fun insertNode(item: Node)

    /**
     * Delete item from the data source
     */
    suspend fun deleteNode(item: Node)

    /**
     * Update item in the data source
     */
    suspend fun updateNode(item: Node)
}
