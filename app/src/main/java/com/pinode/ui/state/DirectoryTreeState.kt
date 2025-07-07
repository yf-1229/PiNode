package com.pinode.ui.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.pinode.model.FileSystemItem
import com.pinode.model.SampleFileSystemData

/**
 * State management for directory tree view
 */
class DirectoryTreeState {
    private val _expandedItems: SnapshotStateMap<String, Boolean> = mutableStateMapOf()
    
    /**
     * Get expanded state for an item
     */
    fun isExpanded(itemId: String): Boolean {
        return _expandedItems[itemId] ?: false
    }
    
    /**
     * Toggle expanded state for an item
     */
    fun toggleExpanded(itemId: String) {
        _expandedItems[itemId] = !(_expandedItems[itemId] ?: false)
    }
    
    /**
     * Set expanded state for an item
     */
    fun setExpanded(itemId: String, expanded: Boolean) {
        _expandedItems[itemId] = expanded
    }
    
    /**
     * Expand all items
     */
    fun expandAll(root: FileSystemItem) {
        expandRecursively(root, true)
    }
    
    /**
     * Collapse all items
     */
    fun collapseAll(root: FileSystemItem) {
        expandRecursively(root, false)
    }
    
    /**
     * Recursively expand/collapse items
     */
    private fun expandRecursively(item: FileSystemItem, expanded: Boolean) {
        if (item.isDirectory) {
            _expandedItems[item.id] = expanded
            item.children.forEach { child ->
                expandRecursively(child, expanded)
            }
        }
    }
    
    /**
     * Get all visible items in the tree (considering expanded state)
     */
    fun getVisibleItems(root: FileSystemItem): List<DirectoryTreeItem> {
        val visibleItems = mutableListOf<DirectoryTreeItem>()
        collectVisibleItems(root, 0, visibleItems)
        return visibleItems
    }
    
    private fun collectVisibleItems(
        item: FileSystemItem,
        depth: Int,
        result: MutableList<DirectoryTreeItem>
    ) {
        result.add(DirectoryTreeItem(item, depth))
        
        if (item.isDirectory && isExpanded(item.id)) {
            item.children.forEach { child ->
                collectVisibleItems(child, depth + 1, result)
            }
        }
    }
}

/**
 * Represents an item in the directory tree with its depth level
 */
data class DirectoryTreeItem(
    val item: FileSystemItem,
    val depth: Int
)

/**
 * ViewModel for managing directory tree state
 */
class DirectoryTreeViewModel {
    private val _treeState = DirectoryTreeState()
    private val _rootItem = SampleFileSystemData.createSampleStructure()
    
    val treeState: DirectoryTreeState get() = _treeState
    val rootItem: FileSystemItem get() = _rootItem
    
    /**
     * Toggle directory expansion
     */
    fun toggleDirectory(itemId: String) {
        _treeState.toggleExpanded(itemId)
    }
    
    /**
     * Get visible items for the current tree state
     */
    fun getVisibleItems(): List<DirectoryTreeItem> {
        return _treeState.getVisibleItems(_rootItem)
    }
    
    /**
     * Expand all directories
     */
    fun expandAll() {
        _treeState.expandAll(_rootItem)
    }
    
    /**
     * Collapse all directories
     */
    fun collapseAll() {
        _treeState.collapseAll(_rootItem)
    }
}