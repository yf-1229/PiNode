package com.pinode.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinode.model.FileSystemItem
import com.pinode.ui.components.DirectoryTreeView
import com.pinode.ui.state.DirectoryTreeViewModel
import kotlinx.coroutines.launch

/**
 * Demo screen for directory tree testing
 */
@Composable
fun DirectoryTreeDemoScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { DirectoryTreeViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Directory Tree Demo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            DirectoryTreeView(
                viewModel = viewModel,
                onFileClick = { file ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Clicked: ${file.name}")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}