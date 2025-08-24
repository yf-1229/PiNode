package com.pinode

import com.pinode.data.NodeStatus
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun nodeStatus_priority_ordering_isCorrect() {
        // Test that NodeStatus priorities are in the correct order
        assertEquals(1, NodeStatus.EMERGENCY.priority)
        assertEquals(2, NodeStatus.FAST.priority)
        assertEquals(3, NodeStatus.WORKING.priority)
        assertEquals(4, NodeStatus.PAUSE.priority)
        assertEquals(5, NodeStatus.DEFAULT.priority)
        assertEquals(6, NodeStatus.CARRYOVER.priority)
        
        // Test ordering
        assertTrue(NodeStatus.EMERGENCY.priority < NodeStatus.FAST.priority)
        assertTrue(NodeStatus.FAST.priority < NodeStatus.WORKING.priority)
        assertTrue(NodeStatus.WORKING.priority < NodeStatus.PAUSE.priority)
        assertTrue(NodeStatus.PAUSE.priority < NodeStatus.DEFAULT.priority)
        assertTrue(NodeStatus.DEFAULT.priority < NodeStatus.CARRYOVER.priority)
    }
}