package com.example.pinode.compose.item

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pinode.PiNodeTopAppBar
import com.example.pinode.R
import com.example.pinode.data.Node
import com.example.pinode.data.NodeStatus
import com.example.pinode.navigation.NavigationDestination
import com.example.pinode.ui.AppViewModelProvider
import com.example.pinode.ui.theme.PiNodeTheme
import kotlinx.coroutines.launch


object NodeDetailsDestination : NavigationDestination {
    override val route = "node_details"
    override val titleRes = R.string.node_detail_title
    const val nodeIdArg = "nodeId"
    val routeWithArgs = "$route/{$nodeIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDetailsScreen( // Dialogにする
    navigateToEditNode: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NodeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PiNodeTopAppBar(
                title = stringResource(NodeDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditNode(uiState.value.nodeDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_node_title),
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        NodeDetailsBody(
            nodeDetailsUiState = uiState.value,
            onComplete = {
                coroutineScope.launch {
                    viewModel.completeNode()
                    navigateBack()
                }
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteNode()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(layoutDirection = LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun NodeDetailsBody(
    nodeDetailsUiState: NodeDetailsUiState,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
   Column(
       modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small)),
       verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
   ) {
       var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

       NodeDetails(
           node = nodeDetailsUiState.nodeDetails.toNode(), modifier = Modifier.fillMaxWidth()
       )
       Button(
           onClick = onComplete,
           modifier = Modifier.fillMaxWidth(),
           shape = MaterialTheme.shapes.small,
       ) {
           Text(stringResource(R.string.complete))
       }
       OutlinedButton(
           onClick = { deleteConfirmationRequired = true },
           shape = MaterialTheme.shapes.small,
           modifier = Modifier.fillMaxWidth()
       ) {
           Text(stringResource(R.string.delete))
       }
       if (deleteConfirmationRequired) {
           DeleteConfirmationDialog(
               onDeleteConfirm = {
                   deleteConfirmationRequired = false
                   onDelete()
               },
               onDeleteCancel = { deleteConfirmationRequired = false },
               modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
           )
       }
   }
}

@Composable
fun NodeDetails(
    node: Node, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            NodeDetailsRow(
                labelResID = R.string.node,
                itemDetail = node.title,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            NodeDetailsRow(
                labelResID = R.string.quantity_in_stock,
                itemDetail = node.description,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
        }

    }
}


@Composable
private fun NodeDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    PiNodeTheme {
        NodeDetailsBody(NodeDetailsUiState(nodeDetails = NodeDetails(1, NodeStatus.RED, "Test")
        ), onComplete = {}, onDelete = {})
    }
}