package com.pinode.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinode.model.FileSystemItem
import com.pinode.model.FileType
import com.pinode.ui.state.DirectoryTreeItem

/**
 * Individual directory item component
 */
@Composable
fun DirectoryItem(
    treeItem: DirectoryTreeItem,
    isExpanded: Boolean,
    onToggle: (String) -> Unit,
    onItemClick: (FileSystemItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val item = treeItem.item
    val depth = treeItem.depth
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (item.isDirectory) {
                    onToggle(item.id)
                } else {
                    onItemClick(item)
                }
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indentation based on depth
        Spacer(modifier = Modifier.width((depth * 16).dp))
        
        // Expansion indicator for directories
        if (item.isDirectory) {
            ExpandCollapseIcon(
                isExpanded = isExpanded,
                modifier = Modifier.size(16.dp)
            )
        } else {
            // Empty space for files to align with directories
            Spacer(modifier = Modifier.size(16.dp))
        }
        
        Spacer(modifier = Modifier.width(4.dp))
        
        // File/Directory icon
        Icon(
            imageVector = getIconForItem(item),
            contentDescription = null,
            tint = getIconColor(item),
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Item name
        Text(
            text = item.name,
            fontSize = 14.sp,
            fontWeight = if (item.isDirectory) FontWeight.Medium else FontWeight.Normal,
            color = if (item.isDirectory) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

/**
 * Animated expand/collapse icon
 */
@Composable
private fun ExpandCollapseIcon(
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearEasing
        ),
        label = "expand_collapse_rotation"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(14.dp)
                .rotate(rotationAngle)
        )
    }
}

/**
 * Get appropriate icon for file system item
 */
private fun getIconForItem(item: FileSystemItem): ImageVector {
    return if (item.isDirectory) {
        Icons.Filled.Folder
    } else {
        when (FileType.fromExtension(item.extension)) {
            FileType.KOTLIN -> Icons.Filled.Code
            FileType.JAVA -> Icons.Filled.Code
            FileType.XML -> Icons.Filled.Article
            FileType.GRADLE -> Icons.Filled.Settings
            FileType.MARKDOWN -> Icons.Filled.Description
            FileType.TEXT -> Icons.Filled.Article
            FileType.JSON -> Icons.Filled.Article
            FileType.YAML -> Icons.Filled.Article
            FileType.PROPERTIES -> Icons.Filled.Settings
            FileType.OTHER -> Icons.Filled.InsertDriveFile
        }
    }
}

/**
 * Get appropriate color for file system item icon
 */
private fun getIconColor(item: FileSystemItem): Color {
    return if (item.isDirectory) {
        Color(0xFF4CAF50) // Green for directories
    } else {
        when (FileType.fromExtension(item.extension)) {
            FileType.KOTLIN -> Color(0xFF7F52FF) // Purple for Kotlin
            FileType.JAVA -> Color(0xFFF89820) // Orange for Java
            FileType.XML -> Color(0xFF42A5F5) // Blue for XML
            FileType.GRADLE -> Color(0xFF02303A) // Dark teal for Gradle
            FileType.MARKDOWN -> Color(0xFF000000) // Black for Markdown
            FileType.TEXT -> Color(0xFF757575) // Gray for text files
            FileType.JSON -> Color(0xFFFFB300) // Amber for JSON
            FileType.YAML -> Color(0xFF607D8B) // Blue gray for YAML
            FileType.PROPERTIES -> Color(0xFF795548) // Brown for properties
            FileType.OTHER -> Color(0xFF9E9E9E) // Gray for other files
        }
    }
}