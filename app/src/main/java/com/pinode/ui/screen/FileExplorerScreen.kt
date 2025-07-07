package com.pinode.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinode.R
import com.pinode.model.FileSystemItem
import com.pinode.ui.components.DirectoryTreeView
import com.pinode.ui.state.DirectoryTreeViewModel
import kotlinx.coroutines.launch

/**
 * File explorer screen with directory tree view
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileExplorerScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel = remember { DirectoryTreeViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "File Explorer",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { paddingValues ->
        FileExplorerContent(
            viewModel = viewModel,
            onFileClick = { file ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Clicked: ${file.name}")
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

/**
 * Content of the file explorer screen
 */
@Composable
private fun FileExplorerContent(
    viewModel: DirectoryTreeViewModel,
    onFileClick: (FileSystemItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DirectoryTreeView(
            viewModel = viewModel,
            onFileClick = onFileClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Standalone file explorer component for embedding in other screens
 */
@Composable
fun FileExplorerComponent(
    onFileClick: (FileSystemItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val viewModel = remember { DirectoryTreeViewModel() }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DirectoryTreeView(
                viewModel = viewModel,
                onFileClick = onFileClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Destination for navigation
 */
object FileExplorerDestination {
    const val route = "file_explorer"
    const val titleRes = R.string.file_explorer_title
}