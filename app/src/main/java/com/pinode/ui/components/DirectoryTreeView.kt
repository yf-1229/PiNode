package com.pinode.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinode.model.FileSystemItem
import com.pinode.ui.state.DirectoryTreeViewModel

/**
 * Main directory tree view component
 */
@Composable
fun DirectoryTreeView(
    viewModel: DirectoryTreeViewModel,
    onFileClick: (FileSystemItem) -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val visibleItems = remember(viewModel.treeState) {
        viewModel.getVisibleItems()
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header with controls
        DirectoryTreeHeader(
            onExpandAll = { viewModel.expandAll() },
            onCollapseAll = { viewModel.collapseAll() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        // Directory tree content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(
                    items = visibleItems,
                    key = { _, item -> item.item.id }
                ) { index, treeItem ->
                    AnimatedDirectoryItem(
                        treeItem = treeItem,
                        isExpanded = viewModel.treeState.isExpanded(treeItem.item.id),
                        onToggle = { itemId -> viewModel.toggleDirectory(itemId) },
                        onItemClick = onFileClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Header with expand/collapse controls
 */
@Composable
private fun DirectoryTreeHeader(
    onExpandAll: () -> Unit,
    onCollapseAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Project Explorer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Row {
            TextButton(
                onClick = onExpandAll
            ) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "Expand All"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Expand All", fontSize = 12.sp)
            }
            
            TextButton(
                onClick = onCollapseAll
            ) {
                Icon(
                    imageVector = Icons.Filled.ExpandLess,
                    contentDescription = "Collapse All"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Collapse All", fontSize = 12.sp)
            }
        }
    }
}

/**
 * Animated directory item with smooth transitions
 */
@Composable
private fun AnimatedDirectoryItem(
    treeItem: com.pinode.ui.state.DirectoryTreeItem,
    isExpanded: Boolean,
    onToggle: (String) -> Unit,
    onItemClick: (FileSystemItem) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = 150,
                easing = LinearEasing
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = 150,
                easing = LinearEasing
            )
        )
    ) {
        DirectoryItem(
            treeItem = treeItem,
            isExpanded = isExpanded,
            onToggle = onToggle,
            onItemClick = onItemClick,
            modifier = modifier
        )
    }
}

/**
 * Compact directory tree view for smaller spaces
 */
@Composable
fun CompactDirectoryTreeView(
    viewModel: DirectoryTreeViewModel,
    onFileClick: (FileSystemItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val visibleItems = remember(viewModel.treeState) {
        viewModel.getVisibleItems()
    }
    
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(
            items = visibleItems,
            key = { item -> item.item.id }
        ) { treeItem ->
            DirectoryItem(
                treeItem = treeItem,
                isExpanded = viewModel.treeState.isExpanded(treeItem.item.id),
                onToggle = { itemId -> viewModel.toggleDirectory(itemId) },
                onItemClick = onFileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
    }
}