package com.example.pinode.data

data class Node(
    val id: Long,
    val status: NodeStatus,
    val icon: Int,
    val title: String,
    val description: String,
)
