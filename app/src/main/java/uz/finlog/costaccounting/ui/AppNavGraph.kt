package uz.finlog.costaccounting.ui


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import org.koin.androidx.compose.koinViewModel
import uz.finlog.costaccounting.ui.screens.home.HomeScreen
import uz.finlog.costaccounting.ui.screens.home.HomeViewModel
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreen
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreenViewModel
import uz.finlog.costaccounting.ui.screens.stats.StatsScreen
import uz.finlog.costaccounting.ui.screens.stats.StatsScreenViewModel

@Composable
fun AppNavGraph(innerPadding: PaddingValues, navController: NavHostController) {

    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
        composable(ScreenRoute.Home.route) {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(ScreenRoute.Stats.route) {
            val viewModel: StatsScreenViewModel = koinViewModel()
            StatsScreen(navController = navController, viewModel)
        }
        composable(ScreenRoute.Add.route) {
            val viewModel: AddExpenseScreenViewModel = koinViewModel()
            AddExpenseScreen(navController = navController, viewModel = viewModel)
        }
    }
}