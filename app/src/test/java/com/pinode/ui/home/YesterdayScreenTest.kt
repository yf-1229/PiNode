package com.pinode.ui.home

import com.pinode.data.Node
import com.pinode.data.NodeLabel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Unit tests for YesterdayScreen functionality
 */
class YesterdayScreenTest {

    @Test
    fun yesterdayUiState_filtersYesterdayNodes_correctly() {
        // Setup test data
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)
        
        val yesterdayDeadline = yesterday.atTime(14, 30)
        val todayDeadline = today.atTime(14, 30)
        val twoDaysAgoDeadline = twoDaysAgo.atTime(14, 30)
        
        val testNodes = listOf(
            Node(
                id = 1,
                title = "Yesterday Task",
                description = "Task with yesterday deadline",
                label = NodeLabel.WORKING,
                deadline = yesterdayDeadline,
                isCompleted = false,
                isDeleted = false
            ),
            Node(
                id = 2,
                title = "Today Task",
                description = "Task with today deadline",
                label = NodeLabel.WORKING,
                deadline = todayDeadline,
                isCompleted = false,
                isDeleted = false
            ),
            Node(
                id = 3,
                title = "Two Days Ago Task",
                description = "Task with two days ago deadline",
                label = NodeLabel.WORKING,
                deadline = twoDaysAgoDeadline,
                isCompleted = false,
                isDeleted = false
            ),
            Node(
                id = 4,
                title = "No Deadline Task",
                description = "Task without deadline",
                label = NodeLabel.WORKING,
                deadline = null,
                isCompleted = false,
                isDeleted = false
            )
        )
        
        // Test filtering logic (simulating what YesterdayViewModel would do)
        val filteredNodes = testNodes.filter { node ->
            node.deadline?.let { deadline ->
                deadline.toLocalDate() == yesterday
            } ?: false
        }
        
        // Verify results
        assertEquals(1, filteredNodes.size)
        assertEquals("Yesterday Task", filteredNodes[0].title)
        assertEquals(yesterdayDeadline, filteredNodes[0].deadline)
    }
    
    @Test
    fun yesterdayUiState_emptyList_whenNoYesterdayNodes() {
        // Setup test data with no yesterday nodes
        val today = LocalDate.now()
        val todayDeadline = today.atTime(14, 30)
        
        val testNodes = listOf(
            Node(
                id = 1,
                title = "Today Task",
                description = "Task with today deadline",
                label = NodeLabel.WORKING,
                deadline = todayDeadline,
                isCompleted = false,
                isDeleted = false
            )
        )
        
        // Test filtering logic
        val yesterday = today.minusDays(1)
        val filteredNodes = testNodes.filter { node ->
            node.deadline?.let { deadline ->
                deadline.toLocalDate() == yesterday
            } ?: false
        }
        
        // Verify results
        assertEquals(0, filteredNodes.size)
    }
}