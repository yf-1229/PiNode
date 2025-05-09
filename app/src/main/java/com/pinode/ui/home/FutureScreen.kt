package com.pinode.ui.home

object AfterTomorrowDestination : NavigationDestination {
    override val route = "aftertomorrow"
    override val titleRes = R.string.today_title
} 

@Composable
fun AfterTomorrowScreen(
    navigateToNodeEntry: () -> Unit,
    navigateToNodeEdit: (Int) -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {
  
}