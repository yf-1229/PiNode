package com.example.pinode.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pinode.PiNodeApplication
import com.example.pinode.compose.home.HomeViewModel
import com.example.pinode.compose.item.NodeDetailsViewModel
import com.example.pinode.compose.item.NodeEditViewModel
import com.example.pinode.compose.item.NodeEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            NodeEditViewModel(
                this.createSavedStateHandle(),
                pinodeApplication().container.nodesRepository
            )
        }
        // Initializer for NodeEntryViewModel
        initializer {
            NodeEntryViewModel(pinodeApplication().container.nodesRepository)
        }

        // Initializer for NodeDetailsViewModel
        initializer {
            NodeDetailsViewModel(
                this.createSavedStateHandle(),
                pinodeApplication().container.nodesRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(pinodeApplication().container.nodesRepository)
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance ofn].
 */
fun CreationExtras.pinodeApplication(): PiNodeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PiNodeApplication)