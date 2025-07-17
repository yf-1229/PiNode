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
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


object NodeAddDestination : NavigationDestination {
    override val route = "node_add"
    override val titleRes = R.string.node_entry_title
}

object NodeAddNotToDoDestination : NavigationDestination {
    override val route = "node_add_not"
    override val titleRes = R.string.node_entry_title // TODO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeAddScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    toDo: Boolean, 
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
            toDo = toDo,
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
    toDo: Boolean, 
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
    
        var deadline by remember { mutableStateOf<LocalDateTime?>(null) }
        remember {
            onNodeValueChange(nodeUiState.nodeDetails.copy(deadline = deadline))
            true // Rememberブロックに値を返す
        }
        
        if (toDo) {
            Text("What to do?",
                fontSize = 60.sp, fontFamily = FontFamily.Serif,
                style = TextStyle.Default.copy(
                    lineBreak = LineBreak.Heading
                )
            )
        } else if (!toDo) {
            Text("What not to do?",
                fontSize = 50.sp, fontFamily = FontFamily.Serif,
                style = TextStyle.Default.copy(
                    lineBreak = LineBreak.Heading
                )
            )
        }
        

        NodeAddInputForm(
            nodeDetails = nodeUiState.nodeDetails,
            onValueChange = onNodeValueChange,
            deadline = { selectedDeadline: LocalDateTime? ->
                deadline = selectedDeadline
                onNodeValueChange(nodeUiState.nodeDetails.copy(deadline = selectedDeadline))
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
    deadline: (LocalDateTime?) -> Unit,
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
        PickerChip(deadline = deadline)
    }
}


@Composable
fun PickerChip(deadline: (LocalDateTime?) -> Unit) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    Row(modifier = Modifier.padding(horizontal =12.dp)) {
        DatePickerChip(
            selectedDateChange = { selectedDate = it }
        )
        TimePickerChip(
            selectedTimeChange = { selectedTime = it }
        )
    }

    if (selectedTime == null && selectedDate == null) {
        deadline(null) // 期限なし
    } else if (selectedTime != null) {
        selectedDate?.let { date ->
            val selectedDeadline: LocalDateTime = date.atTime(selectedTime)
            deadline(selectedDeadline)
        }
    } else if (selectedTime == null) {
        selectedDate?.let { date ->
            val selectedDeadlineAllDay: LocalDateTime = date.atTime(23, 59, 59)
            deadline(selectedDeadlineAllDay)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerChip(
    selectedDateChange: (LocalDate?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    var selectedDateState by remember { mutableStateOf<LocalDate?>(null) }
    val datePickerState = rememberDatePickerState()


    AssistChip(
        onClick = { showModal = true },
        label = {
            if (selectedDateState != null) {
                Text(selectedDateState.toString())
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
                    selectedDateState = datePickerState.selectedDateMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() }
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
    selectedTimeChange: (LocalTime?) -> Unit
) {
    var selectedTimeState by remember { mutableStateOf<LocalTime?>(null) }
    var showDial by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { showDial = true },
        label = {
            if (selectedTimeState != null) {
                Text(selectedTimeState.toString())
            } else {
                Text("Time")
            }
        },
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
        Dialog(onDismissRequest = { showDial = false }) {
            Card {
                Column {
                    TimePicker(
                        state = timePickerState,
                    )
                    Button(onClick = { showDial = false }) {
                        Text("Dismiss picker")
                    }
                    Button(onClick = {
                        selectedTimeChange(
                            LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute,
                                0
                            )
                        )
                        selectedTimeState = LocalTime.of(
                            timePickerState.hour,
                            timePickerState.minute,
                            0
                        )
                        showDial = false
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}