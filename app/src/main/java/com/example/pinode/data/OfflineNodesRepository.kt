package com.example.pinode.data

import kotlinx.coroutines.flow.Flow

class OfflineNodesRepository(private val nodeDao: NodeDao) : NodesRepository {
    override fun getAllNodesStream(): Flow<List<Node>> = nodeDao.getAllItems()

    override fun getNodeStream(id: Int): Flow<Node?> = nodeDao.getNode(id)

    override suspend fun insertNode(item: Node) = nodeDao.insertNode(item)

    override suspend fun deleteNode(item: Node) = nodeDao.deleteNode(item)

    override suspend fun updateNode(item: Node) = nodeDao.updateNode(item)
}