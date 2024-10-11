package com.example.pinode.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: NodeStatus,
    val geoX: Int,
    val geoY: Int,
    val icon: Int,
    val title: String,
    val description: String,
)

enum class NodeStatus { // Node's color
    Red, Yellow, Green, Gray,
}

@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNode(node: Node)
    @Update
    suspend fun updateNode(node: Node)
    @Delete
    suspend fun deleteNode(node: Node)
    @Query("SELECT * from nodes WHERE id = :id")
    fun getNode(id: Int): Flow<Node>
    @Query("SELECT * from nodes ORDER BY title ASC")
    fun getAllItems(): Flow<List<Node>>
}