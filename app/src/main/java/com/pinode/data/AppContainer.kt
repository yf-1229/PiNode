package com.pinode.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val nodesRepository: NodesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [NodesRepository]
     */
    override val nodesRepository: NodesRepository by lazy {
        OfflineNodesRepository(PiNodeDatabase.getDatabase(context).nodeDao())
    }
}