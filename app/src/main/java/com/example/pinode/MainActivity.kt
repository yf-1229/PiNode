package com.example.pinode

import PiNodeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.pinode.ui.theme.PiNodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiNodeTheme {
                PiNodeScreen()
            }
        }
    }
}

@Preview
@Composable
fun LineCrossingsPreview() {
    PiNodeScreen()
}