package com.example.pinode.compose.item

import android.window.BackEvent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.R
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import org.jetbrains.annotations.ApiStatus.Experimental

object NodeDetailsDestination : NavigationDestination {
    override val route = "node_details"
    override val titleRes = R.string.node_details_title
    const val nodeIdArg = "nodeId"
    val routeWithArgs = "$route/{$nodeIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDetailsScreen(
    navigateToEditNode: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NodeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(NodeDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        NodeDetailsBody(
            itemDetailsUiState = uiState.value,
            onSellItem = { viewModel.reduceQuantityByOne() },
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}