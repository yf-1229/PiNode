package com.pinode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import com.pinode.ui.theme.PiNodeTheme
import androidx.compose.ui.Modifier


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PiNodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    ) {
                    PiNodeApp()
                }
            }
        }
    }
}
