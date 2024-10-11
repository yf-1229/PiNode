package com.example.pinode.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pinode.NodeApplication
import com.example.pinode.compose.home.HomeViewModel
import com.example.pinode.data.Node

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(nodeApplication().container.nodesRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.nodeApplication(): NodeApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NodeApplication)