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
    val status: NodeStatus,
    val deadline: LocalDateTime?,
    val label: Boolean,
    var isCompleted: Boolean,
    val isDeleted: Boolean,
)


enum class NodeStatus(val color: Int, val text: String, val priority: Int) {
    PAUSE(R.color.YELLOW, "Pause", 3),
    WORKING(R.color.GREEN, "Working", 2),
    FAST(R.color.BLUE, "Fast", 1),
    CARRYOVER(R.color.PURPLE, "Carry Over", 5),
    DEFAULT(R.color.teal_700, "Default", 4),
    NOTTODO(R.color.WHITE, "Not to do", 6),
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