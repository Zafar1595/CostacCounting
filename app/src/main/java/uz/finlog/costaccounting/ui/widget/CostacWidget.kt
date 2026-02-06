package uz.finlog.costaccounting.ui.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.*
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.color.ColorProvider
import uz.finlog.costaccounting.MainActivity
import uz.finlog.costaccounting.R
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.action.actionStartActivity

class CostacWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent()
        }
    }

    @Composable
    private fun WidgetContent() {
        // В будущем здесь будут реальные данные из State
        val prefs = currentState<Preferences>()

        val total = prefs[CostacWidgetKeys.TOTAL_AMOUNT] ?: 0.0
        val monthName = prefs[CostacWidgetKeys.MONTH_NAME] ?: ""
        val currency = prefs[CostacWidgetKeys.CURRENCY_SYMBOL] ?: ""
        // Форматируем число: 1250000.0 -> "1 250 000"
        val formattedTotal = "%,.0f".format(total).replace(',', ' ')

        val intent = Intent(
            Intent.ACTION_VIEW,
            "costaccounting://home.add_new_expense".toUri(),
            LocalContext.current,
            MainActivity::class.java
        )

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp)
                .background(ColorProvider(
                    day = Color(0xFFF3F3F3),
                    night = Color(0xFF1A1A1A)
                )),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            // Блок суммы
            Column(
                modifier = GlanceModifier.defaultWeight(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    text = monthName.uppercase(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(day = Color.Gray, night = Color.LightGray)
                    )
                )
                Spacer(modifier = GlanceModifier.height(4.dp))
                Text(
                    text = "$formattedTotal $currency",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(day = Color.Black, night = Color.White)
                    )
                )
            }

            // Кнопка добавления снизу
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        ColorProvider(
                            day = Color(0xFF6200EE), // Твой основной цвет (Primary)
                            night = Color(0xFFBB86FC)
                        )
                    )
                    .cornerRadius(24.dp) // Скругляем саму кнопку
                    .clickable(onClick = actionStartActivity(intent = intent)),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Image(
                    provider = ImageProvider(R.drawable.outline_add_24), // Убедись, что иконка есть
                    contentDescription = null,
                    modifier = GlanceModifier.size(20.dp),
                    colorFilter = ColorFilter.tint(ColorProvider(day = Color.White, night = Color.Black))
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(
                    text = "Добавить расход",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(Color.White, Color.Black)
                    )
                )
            }
        }
    }

    object CostacWidgetKeys {
        val TOTAL_AMOUNT = doublePreferencesKey("total_amount")
        val MONTH_NAME = stringPreferencesKey("month_name")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
    }
}