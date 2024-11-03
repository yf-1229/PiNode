package com.example.pinode.data

import androidx.compose.ui.graphics.colorspace.Rgb
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
    val icon: Int,
    val title: String,
    val description: String,
)

enum class NodeStatus(val rgb: Int){ // Node's color
    RED(0xFF0000), YELLOW(0xFFF00), GREEN(0x008000), GRAY(0x808080), BLACK(0x000000)
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