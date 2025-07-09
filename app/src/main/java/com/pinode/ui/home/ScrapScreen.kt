package com.pinode.ui.home

import androidx.compose.runtime.Composable
import com.pinode.R
import com.pinode.ui.navigation.NavigationDestination

object ScrapDestination : NavigationDestination {
    override val route = "scrap"
    override val titleRes = R.string.today_title // TODO
}

@Composable
fun ScrapScreen() {

}