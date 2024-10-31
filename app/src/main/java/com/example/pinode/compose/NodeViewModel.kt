package com.example.pinode.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pinode.data.Node
import java.util.UUID

class TaskViewModel : ViewModel() {
    private val _nodes = mutableStateListOf<Node>()
    val nodes: List<Node> get() = _nodes

    fun addNode(title: String) {
        val newTask = Node(id = UUID.randomUUID().toString(), title = title)
        _tasks.add(newTask)
    }

    fun deleteTask(taskId: String) {

        if (taskIndex != -1) {
            val task = _tasks[taskIndex].copy(isDeleted = true)
            _tasks[taskIndex] = task // タスクを更新して削除フラグを立てる
        }
    }

    fun getActiveTasks(): List<Node> {
        return _tasks.filter { !it.isDeleted }
    }

    fun getDeletedTasks(): List<Node> {
        return _tasks.filter { it.isDeleted }
    }
}
