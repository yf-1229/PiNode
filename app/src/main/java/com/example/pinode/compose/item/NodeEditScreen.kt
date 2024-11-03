package com.example.pinode.compose.item

import com.example.pinode.R
import com.example.pinode.navigation.NavigationDestination

object NodeEditDestination : NavigationDestination {
    override val route = "node_edit"
    override val titleRes = R.string.edit_item_title
    const val nodeIdArg = "nodeId"
    val routeWithArgs = "$route/{$nodeIdArg}"
}