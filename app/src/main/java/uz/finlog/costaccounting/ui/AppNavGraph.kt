package uz.finlog.costaccounting.ui


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import uz.finlog.costaccounting.ui.screens.home.HomeScreen
import uz.finlog.costaccounting.ui.screens.home.HomeViewModel
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreen
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreenViewModel
import uz.finlog.costaccounting.ui.screens.home.detail.DetailScreen
import uz.finlog.costaccounting.ui.screens.home.detail.DetailViewModel
import uz.finlog.costaccounting.ui.screens.home.detail.edit.EditExpenseScreen
import uz.finlog.costaccounting.ui.screens.settings.SettingsScreen
import uz.finlog.costaccounting.ui.screens.settings.SettingsViewModel
import uz.finlog.costaccounting.ui.screens.stats.StatsScreen
import uz.finlog.costaccounting.ui.screens.stats.StatsScreenViewModel

@Composable
fun AppNavGraph(innerPadding: PaddingValues, navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
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
        composable(ScreenRoute.Settings.route) {
            val viewModel: SettingsViewModel = koinViewModel()
            SettingsScreen(viewModel = viewModel)
        }
        composable(route = "${ScreenRoute.Detail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })) { backStackEntry ->
            val viewModel: DetailViewModel = koinViewModel()
            val expenseId = backStackEntry.arguments?.getInt("id") ?: return@composable
            DetailScreen(viewModel = viewModel, expenseId, navController)
        }
        composable(
            route = "${ScreenRoute.Edit.route}/{expenseId}",
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: return@composable
            val viewModel: DetailViewModel = koinViewModel()
            EditExpenseScreen(
                viewModel = viewModel,
                expenseId = expenseId,
                navController = navController
            )
        }
    }
}