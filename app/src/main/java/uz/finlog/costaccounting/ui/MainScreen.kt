package uz.finlog.costaccounting.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.androidx.compose.koinViewModel
import uz.finlog.costaccounting.ui.screens.settings.SettingsViewModel
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import uz.finlog.costaccounting.util.AppConstants.setSelectedCurrency

sealed class ScreenRoute(
    val name: String,
    val route: String,
    val icon: ImageVector?
) {
    data object Home : ScreenRoute("Главная", "home", Icons.Default.Home)
    data object Stats : ScreenRoute("Статистика", "stats", Icons.Outlined.BarChart)
    data object Add : ScreenRoute("Добавить", "add", Icons.Default.Add)
    data object Settings: ScreenRoute("Настройки", "settings", Icons.Default.Settings)
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = koinViewModel()
    setSelectedCurrency(settingsViewModel.selectedCurrency.collectAsState().value)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    icon = {
                        Icon(
                            ScreenRoute.Home.icon ?: Icons.Default.Abc,
                            contentDescription = ScreenRoute.Home.name
                        )
                    },
                    label = { Text(ScreenRoute.Home.name) },
                    selected = currentRoute == ScreenRoute.Home.route,
                    onClick = {
                        if (currentRoute != ScreenRoute.Home.route) {
                            navController.navigate(ScreenRoute.Home.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            ScreenRoute.Stats.icon ?: Icons.Default.Abc,
                            contentDescription = ScreenRoute.Stats.name
                        )
                    },
                    label = { Text(ScreenRoute.Stats.name) },
                    selected = currentRoute == ScreenRoute.Stats.route,
                    onClick = {
                        if (currentRoute != ScreenRoute.Stats.route) {
                            navController.navigate(ScreenRoute.Stats.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            ScreenRoute.Settings.icon ?: Icons.Default.Settings,
                            contentDescription = ScreenRoute.Stats.name
                        )
                    },
                    label = { Text(ScreenRoute.Settings.name) },
                    selected = currentRoute == ScreenRoute.Settings.route,
                    onClick = {
                        if (currentRoute != ScreenRoute.Settings.route) {
                            navController.navigate(ScreenRoute.Settings.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            val currentBackStackEntry = navController.currentBackStackEntryAsState()
            if (currentBackStackEntry.value?.destination?.route == ScreenRoute.Home.route)
                FloatingActionButton(
                    onClick = {
                        navController.navigate(ScreenRoute.Add.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                ) {
                    Icon(
                        ScreenRoute.Add.icon ?: Icons.Default.Add,
                        contentDescription = ScreenRoute.Add.name
                    )
                }
        }
    ) { innerPadding ->
        AppNavGraph(innerPadding, navController)
    }
}
