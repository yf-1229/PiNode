package com.example.pinode

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pinode.data.Node
import com.pinode.data.NodeDao
import com.pinode.data.PiNodeDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NodeDaoTest {
    private lateinit var nodeDao: NodeDao
    private lateinit var pinodeDatabase: PiNodeDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        pinodeDatabase = inMemoryDatabaseBuilder(context, PiNodeDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        nodeDao = pinodeDatabase.nodeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        pinodeDatabase.close()
    }

    private var node1 = Node(1, NodeStatus.RED, "Test1", "test", isCompleted = false, isDeleted = false)
    private var node2 = Node(2, NodeStatus.BLACK, "Test2", "test2", isCompleted = false, isDeleted = false)

    private suspend fun addOneNodeToDb() {
        nodeDao.insertNode(node1)
    }

    private suspend fun addTwoNodesToDb() {
        nodeDao.insertNode(node1)
        nodeDao.insertNode(node2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsNodeIntoDB() = runBlocking {
        addOneNodeToDb()
        val allItems = nodeDao.getAllItems().first()
        assertEquals(allItems[0], node1)
    }
}
