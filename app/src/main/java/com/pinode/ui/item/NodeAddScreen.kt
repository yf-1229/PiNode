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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale


object NodeAddDestination : NavigationDestination {
    override val route = "node_add"
    override val titleRes = R.string.node_entry_title
}

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
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        var selectedTime by remember { mutableIntStateOf(0) }


        // 初期値として選択されたミニッツに応じてdeadlineを設定
        remember {
            val dateTimeCtrl = DateTimeCtrl()
            onNodeValueChange(nodeUiState.nodeDetails.copy(deadline = ))
            true // Rememberブロックに値を返す
        }

        NodeAddInputForm(
            nodeDetails = nodeUiState.nodeDetails,
            onValueChange = onNodeValueChange,
            selectedDateChange = { date: LocalDate? ->
                selectedDate = date
                onNodeValueChange(nodeUiState.nodeDetails.copy(startDate = date))
            },
            selectedTimeChange = { time: LocalDate? ->
                selectedTime = time// TODO
                val dateTimeCtrl = DateTimeCtrl()
            },
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
    selectedDateChange: (LocalDate?) -> Unit,
    selectedTimeChange: (LocalDate?) -> Unit,
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

        Row {
            DatePickerChip(selectedDateChange)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerChip(
    selectedDateChange: (LocalDate?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()

    val formattedDate = SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedDate)
    AssistChip(
        onClick = { showModal = true },
        label = {
            if (selectedDate != null) {
                Text(formattedDate)
            } else {
                Text("Date")
            }
        },
        leadingIcon = {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = "DayPicker",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )

    if (showModal){
        DatePickerDialog(
            onDismissRequest = { showModal = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateChange(datePickerState.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() })
                    selectedDate = datePickerState.selectedDateMillis
                    showModal = false
                }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showModal = false }
                ) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerChip(
    selectedTimeChange: (LocalDateTime) -> Unit
) {
    val selectedTime by remember { mutableStateOf(LocalDateTime.now()) }
    var showDial by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { showDial = true},
        label = { Text("When is it?") },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.schedule_24),
                contentDescription = "DayPicker",
                Modifier.size(AssistChipDefaults.IconSize),
            )
        }
    )

    if (showDial) {
        val timePickerState = rememberTimePickerState(
            initialHour = LocalDateTime.now().hour,
            initialMinute = LocalDateTime.now().minute,
            is24Hour = true,
        )

        Column {
            TimePicker(
                state = timePickerState,
            )
            Button(onClick = { showDial = false }) {
                Text("Dismiss picker")
            }
            Button(onClick = {
                selectedTimeChange(
                    LocalDateTime(
                        timePickerState.hour.
                    )
                )
                showDial = false
            }) {
                Text("Confirm selection")
            }
        }
    }
    }
}