package com.pinode.ui.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.R
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object NodeEditDestination : NavigationDestination {
    override val route = "node_edit"
    override val titleRes = R.string.edit_node_title
    const val nodeIdArg = "nodeId"
    val routeWithArgs = "$route/{$nodeIdArg}"
}

@Composable
fun NodeEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NodeEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val nodeUiState = viewModel.nodeUiState
    val coroutineScope = rememberCoroutineScope()

    // NodeAddScreenのUIを流用して編集画面を構成
    NodeAddBody(
        nodeUiState = nodeUiState,
        onNodeValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.updateNode()
                navigateBack()
            }
        },
        toDo = true, // TODO ?
        modifier = Modifier
    )
}

