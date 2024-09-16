package com.example.pinode.ui

import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pinode.R

@Composable
fun NodeItem(@DrawableRes taskIcon: Int, modifier: Modifier = Modifier) {
    Card(modifier = Modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null
        )

    }
}

@Preview
@Composable
fun NodeItemPreview() {
    NodeItem()
}