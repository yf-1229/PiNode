package com.pinode.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pinode.PiNodeTopAppBar
import com.pinode.R
import com.pinode.data.NodeLabel
import com.pinode.ui.AppViewModelProvider
import com.pinode.ui.navigation.NavigationDestination
import com.pinode.ui.theme.PiNodeTheme
import kotlinx.coroutines.launch

object NodeAddFastDestination : NavigationDestination {
    override val route = "node_add_fast"
    override val titleRes = R.string.node_entry_title
}

// 共通のオプションリストを定数として定義
private val TIME_OPTIONS = listOf(15, 30, 60) // For Fast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeAddFastScreen(
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
        NodeAddFastBody(
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
fun NodeAddFastBody(
    nodeUiState: NodeUiState,
    onNodeValueChange: (NodeDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        // 初期値を正しくTIME_OPTIONS[0]に設定
        var selectedMinutes by rememberSaveable { mutableIntStateOf(0) }

        Text("Fast!!",
            fontSize = 60.sp, fontFamily = FontFamily.Serif,
            style = TextStyle.Default.copy(
                lineBreak = LineBreak.Heading
            )
        )

        NodeAddFastInputForm(
            nodeDetails = nodeUiState.nodeDetails,
            selectedMinutesChange = { minutes: Int ->
                selectedMinutes = minutes
                val dateTimeCtrl = DateTimeCtrl()
                val deadlineTime = dateTimeCtrl.getDeadlineByMinutes(selectedMinutes = minutes.toLong())
                onNodeValueChange(nodeUiState.nodeDetails.copy(
                    deadline = deadlineTime,
                    label = NodeLabel.FAST
                ))
            },
            onValueChange = onNodeValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,  // 保存時にはすでにdeadlineが更新済み
            enabled = nodeUiState.isEntryValid && selectedMinutes != 0,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun NodeAddFastInputForm(
    nodeDetails: NodeDetails,
    modifier: Modifier = Modifier,
    selectedMinutesChange: (Int) -> Unit,
    onValueChange: (NodeDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
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

        // 初期選択インデックスを0に設定（TIME_OPTIONS[0]の位置）
        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

        // 時間選択UI
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier.fillMaxWidth()
        ){
            TIME_OPTIONS.forEachIndexed { index, minutes ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = TIME_OPTIONS.size
                    ),
                    onClick = {
                        selectedIndex = index
                        // Removed redundant call to onValueChange with unmodified copy
                        // 選択された時間値を渡す
                        selectedMinutesChange(TIME_OPTIONS[index])
                    },
                    selected = index == selectedIndex,
                    label = { Text(minutes.toString()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        
    }
}

@Preview(showBackground = true)
@Composable
fun NodeEditScreenPreview() {
    PiNodeTheme {
        NodeEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}