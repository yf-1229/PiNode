package com.example.pinode.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nodes")
data class Node(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: NodeStatus,
    val icon: Int,
    val title: String,
    val description: String,
)
