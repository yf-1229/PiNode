package com.pinode.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object NodeAddDestination : NavigationDestination {
    override val route = "node_add"
    override val titleRes = R.string.node_entry_title
}

val datePickerState = rememberDatePickerState()
private val PRIORITY_OPTIONS = listOf(1, 2, 3, 0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: NodeEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            PiNodeTopAppBar(
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        NodeAddBody(
            nodeUiState = viewModel.nodeUiState,
            onNodeValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveNode()
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
                .fillMaxWidth()
        )
    }
}

@Composable
fun NodeAddBody(
    nodeUiState: NodeUiState,
    onNodeValueChange: (NodeDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        // 初期値として選択されたミニッツに応じてdeadlineを設定
        remember {
            val dateTimeCtrl = DateTimeCtrl()
            val deadlineTime = dateTimeCtrl.getDeadline(selectedTime = selectedTime.toLong())
            onNodeValueChange(nodeUiState.nodeDetails.copy(deadline = deadlineTime))
            true // Rememberブロックに値を返す
        }

        NodeAddInputForm(
            nodeDetails = nodeUiState.nodeDetails,
            onValueChange = onNodeValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,  // 保存時にはすでにdeadlineが更新済み
            enabled = nodeUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun NodeAddInputForm(
    nodeDetails: NodeDetails,
    modifier: Modifier = Modifier,
    onValueChange: (NodeDetails) -> Unit = {},
    selectedDateChange: (Long) -> Unit,
    selectedTimeChange: (Long) -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = nodeDetails.title,
            onValueChange = { onValueChange(nodeDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.node_title_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = nodeDetails.description,
            onValueChange = { onValueChange(nodeDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.node_description_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Row() {
            DatePickerChip(selectedDateChange)
            TimePickerChip(selectedTimeChange)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerChip(
    selectedDateChange: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    var showModal by remember { mutableStateOf(false) }
    AssistChip(
        onClick = {
            if (showModal) {
                DatePickerModal(
                    onDateSelected = { selectedDateChange = it },
                    onDismiss = { showModal = false}
                )
            }
        },
        label = { Text("What date is it?") },
        leadingIcon = {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "DayPicker",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}

// TODO 日付選択ツール
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


@Composable
fun TimePickerChip(
    selectedTimeChange: (Long) -> Unit
) {
    AssistChip(
        onClick = { }, // TODO 時刻選択ツール
        label = { Text("When is it?") },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.schedule_24),
                contentDescription = "DayPicker",
                Modifier.size(AssistChipDefaults.IconSize),
            )
        }
    )
}

// TODO 時刻選択ツール