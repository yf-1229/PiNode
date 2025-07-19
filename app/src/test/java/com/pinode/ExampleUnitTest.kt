package com.pinode

import com.pinode.data.NodeLabel
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
    fun nodeLabel_contains_default_and_nottodo() {
        // Test that the new NodeLabel values exist
        assertNotNull(NodeLabel.DEFAULT)
        assertNotNull(NodeLabel.NOTTODO)
        
        // Test that DEFAULT maps to GREEN color and NOTTODO maps to RED color
        assertEquals(com.pinode.R.color.GREEN, NodeLabel.DEFAULT.color)
        assertEquals(com.pinode.R.color.RED, NodeLabel.NOTTODO.color)
    }
}