package com.pinode.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Update
import com.pinode.R
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var status: NodeStatus,
    val title: String,
    val description: String,
    val deadline: Instant,
    var isCompleted: Boolean,
    val isDeleted: Boolean,
)


enum class NodeStatus(var color: Int){ // Node's color
    RED(R.color.RED),
    YELLOW(R.color.YELLOW),
    GREEN(R.color.GREEN),
    GRAY(R.color.GRAY)
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