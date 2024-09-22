package com.example.pinode.ui

import android.util.EventLogTags.Description
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pinode.R
import com.example.pinode.data.Node

@Composable
fun NodeItemScreen(@DrawableRes taskIcon: Int, modifier: Modifier = Modifier) {
    Column(modifier = Modifier) {
        NodeItemScreenTopBar(),
        NodeItemDetailsCard()
    }
}

@Composable
private fun NodeItemScreenTopBar(
    onBackButtonClicked: () -> Unit,
    taskUiState: UiState,
    modifier: Modifier = Modifier
) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.navigation_back)
            )
        }
    }
}

@Composable
private fun NodeItemDetailsCard(
    node: Node,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(modifier = Modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
            )
        }
    }
}


@Preview
@Composable
fun NodeItemPreview() {
    NodeItemScreen()
}