package uz.finlog.costaccounting

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import uz.finlog.costaccounting.ui.theme.CostAccountingTheme
import uz.finlog.costaccounting.ui.AppNavGraph
import uz.finlog.costaccounting.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Включаем поддержку edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CostAccountingTheme {
                val isDark = isSystemInDarkTheme()
                val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
                SideEffect {
                    @Suppress("DEPRECATION")
                    window.statusBarColor = surfaceVariant.toArgb()
                    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isDark
                }

                MainScreen()
            }
        }
    }
}
