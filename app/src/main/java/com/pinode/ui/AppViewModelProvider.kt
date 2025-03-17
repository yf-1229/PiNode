package com.pinode.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pinode.PiNodeApplication
import com.pinode.ui.home.HomeViewModel
import com.pinode.ui.item.NodeEditViewModel
import com.pinode.ui.item.NodeEntryViewModel

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

        // Initializer for HomeViewModel
        initializer {

            HomeViewModel(
                this.createSavedStateHandle(),
                pinodeApplication().container.nodesRepository
            )
        }
    }
}
/**
 * Extension function to queries for [Application] object and returns an instance ofn].
 */
fun CreationExtras.pinodeApplication(): PiNodeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PiNodeApplication)