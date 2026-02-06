package uz.finlog.costaccounting.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.androidx.compose.koinViewModel
import uz.finlog.costaccounting.ui.screens.settings.SettingsViewModel
import uz.finlog.costaccounting.util.AppConstants.adUnitId
import uz.finlog.costaccounting.util.AppConstants.setSelectedCurrency

sealed class ScreenRoute(
    val name: String,
    val route: String,
    val icon: ImageVector?
) {
    data object Home : ScreenRoute("Главная", "home", Icons.Default.Home)
    data object Stats : ScreenRoute("Статистика", "stats", Icons.Outlined.BarChart)
    data object Add : ScreenRoute("Добавить", "add", Icons.Default.Add)
    data object Settings : ScreenRoute("Настройки", "settings", Icons.Default.Settings)
    data object Detail : ScreenRoute("Детали расхода", "detail", null) {
        fun routeWithId(id: Int): String = "$route/$id"
    }
    data object Edit : ScreenRoute("Редактирование данных", "edit", null) {
        fun routeWithId(id: Int): String = "$route/$id"
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val currentCurrency by settingsViewModel.selectedCurrency.collectAsState()

    // Передаем валюту в константы только при ее реальном изменении
    LaunchedEffect(currentCurrency) {
        setSelectedCurrency(currentCurrency)
    }

    // Определяем текущий маршрут
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Список элементов нижней навигации (только те, у которых есть иконка)
    val bottomNavItems = remember {
        listOf(ScreenRoute.Home, ScreenRoute.Stats, ScreenRoute.Settings)
    }

    Scaffold(
        bottomBar = {
            // Показываем бар только на главных экранах
            if (currentRoute in bottomNavItems.map { it.route }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AdaptiveAdBanner(adUnitId = adUnitId)

                    NavigationBar {
                        bottomNavItems.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = screen.icon ?: Icons.Default.Abc,
                                        contentDescription = screen.name
                                    )
                                },
                                label = { Text(screen.name) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            // Очищаем стек до начального экрана, чтобы не копить вкладки
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
                    }
                }
            }
        },
        floatingActionButton = {
            // FAB только на главном экране
            if (currentRoute == ScreenRoute.Home.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(ScreenRoute.Add.route) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }
        }
    ) { innerPadding ->
        AppNavGraph(innerPadding, navController)
    }
}

@Composable
fun AdaptiveAdBanner(adUnitId: String) {
    val context = LocalContext.current
    val adSize = remember {
        val displayMetrics = Resources.getSystem().displayMetrics
        val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
//            .height(adSize.height.toDp())
        ,
        factory = {
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId(adUnitId)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

// Вспомогательная функция
fun Int.toDp(): Dp =
    (this / Resources.getSystem().displayMetrics.density).dp