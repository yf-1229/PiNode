package com.pinode.ui.state

import com.pinode.model.FileSystemItem
import com.pinode.model.FileSystemItemType
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for DirectoryTreeState
 */
class DirectoryTreeStateTest {
    
    @Test
    fun testInitialState() {
        val state = DirectoryTreeState()
        assertFalse("Initial state should be collapsed", state.isExpanded("test"))
    }
    
    @Test
    fun testToggleExpanded() {
        val state = DirectoryTreeState()
        
        // Initially collapsed
        assertFalse(state.isExpanded("test"))
        
        // Toggle to expanded
        state.toggleExpanded("test")
        assertTrue(state.isExpanded("test"))
        
        // Toggle back to collapsed
        state.toggleExpanded("test")
        assertFalse(state.isExpanded("test"))
    }
    
    @Test
    fun testSetExpanded() {
        val state = DirectoryTreeState()
        
        // Set to expanded
        state.setExpanded("test", true)
        assertTrue(state.isExpanded("test"))
        
        // Set to collapsed
        state.setExpanded("test", false)
        assertFalse(state.isExpanded("test"))
    }
    
    @Test
    fun testVisibleItemsWithCollapsedRoot() {
        val state = DirectoryTreeState()
        val root = createTestFileSystem()
        
        val visibleItems = state.getVisibleItems(root)
        
        // Should only contain root when collapsed
        assertEquals(1, visibleItems.size)
        assertEquals(root, visibleItems[0].item)
        assertEquals(0, visibleItems[0].depth)
    }
    
    @Test
    fun testVisibleItemsWithExpandedRoot() {
        val state = DirectoryTreeState()
        val root = createTestFileSystem()
        
        // Expand root
        state.setExpanded(root.id, true)
        
        val visibleItems = state.getVisibleItems(root)
        
        // Should contain root and its direct children
        assertTrue("Should have more than 1 item when expanded", visibleItems.size > 1)
        assertEquals(root, visibleItems[0].item)
        assertEquals(0, visibleItems[0].depth)
        
        // Second item should be a child with depth 1
        assertEquals(1, visibleItems[1].depth)
    }
    
    @Test
    fun testExpandAll() {
        val state = DirectoryTreeState()
        val root = createTestFileSystem()
        
        state.expandAll(root)
        
        // All directories should be expanded
        assertTrue(state.isExpanded(root.id))
        root.children.forEach { child ->
            if (child.isDirectory) {
                assertTrue("Directory ${child.id} should be expanded", state.isExpanded(child.id))
            }
        }
    }
    
    @Test
    fun testCollapseAll() {
        val state = DirectoryTreeState()
        val root = createTestFileSystem()
        
        // First expand all
        state.expandAll(root)
        
        // Then collapse all
        state.collapseAll(root)
        
        // All directories should be collapsed
        assertFalse(state.isExpanded(root.id))
        root.children.forEach { child ->
            if (child.isDirectory) {
                assertFalse("Directory ${child.id} should be collapsed", state.isExpanded(child.id))
            }
        }
    }
    
    private fun createTestFileSystem(): FileSystemItem {
        return FileSystemItem(
            id = "root",
            name = "root",
            path = "/",
            type = FileSystemItemType.DIRECTORY,
            children = listOf(
                FileSystemItem(
                    id = "folder1",
                    name = "folder1",
                    path = "/folder1",
                    type = FileSystemItemType.DIRECTORY,
                    children = listOf(
                        FileSystemItem(
                            id = "file1",
                            name = "file1.txt",
                            path = "/folder1/file1.txt",
                            type = FileSystemItemType.FILE
                        )
                    )
                ),
                FileSystemItem(
                    id = "file2",
                    name = "file2.txt",
                    path = "/file2.txt",
                    type = FileSystemItemType.FILE
                )
            )
        )
    }
}