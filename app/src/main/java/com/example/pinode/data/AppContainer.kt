package com.example.pinode.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val nodesRepository: NodesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [NodesRepository]
     */
    override val nodesRepository: NodesRepository by lazy {
        OfflineNodesRepository(NodeDatabase.getDatabase(context).nodeDao())
    }
}