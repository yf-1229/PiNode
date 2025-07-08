package com.pinode.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.pinode.R
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val label: NodeLabel?,
    val deadline: LocalDateTime?,
    var isCompleted: Boolean,
    val isDeleted: Boolean,
)


enum class NodeLabel(var color: Int) {
    EMERGENCY(R.color.RED),
    PAUSE(R.color.ORANGE),
    WORKING(R.color.YELLOW),
    FAST(R.color.BLUE),
    CARRYOVER(R.color.PURPLE),
    DEFAULT(R.color.GREEN),
    COMPLETE(R.color.GRAY)
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
    fun getNode(id: Int): Flow<Node?>

    @Query("SELECT * from nodes ORDER BY title ASC")
    fun getAllItems(): Flow<List<Node>>

}