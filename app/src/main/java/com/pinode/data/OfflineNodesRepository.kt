package com.pinode.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineNodesRepository(private val nodeDao: NodeDao) : NodesRepository {
    override fun getAllNodesStream(): Flow<List<Node>> = 
        nodeDao.getAllItems().map { nodes ->
            nodes.sortedWith(compareBy<Node> { it.status.priority }.thenBy { it.title })
        }

    override fun getNodeStream(id: Int): Flow<Node?> = nodeDao.getNode(id)

    override suspend fun insertNode(item: Node) = nodeDao.insertNode(item)

    override suspend fun deleteNode(item: Node) = nodeDao.deleteNode(item)

    override suspend fun updateNode(item: Node) = nodeDao.updateNode(item)
}