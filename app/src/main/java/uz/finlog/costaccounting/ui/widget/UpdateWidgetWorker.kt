package uz.finlog.costaccounting.ui.widget

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.foundation.gestures.Orientation
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.domain.ExpenseRepository
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class UpdateWidgetWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: ExpenseRepository,
    private val sharedPref: UserPreferences
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Получаем данные
            val startOfMonth = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                // обнуляем часы/минуты...
            }.timeInMillis

            val total = repository.getTotalSpentSince(startOfMonth)
            val monthName = LocalDate.now().month
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")) // Standalone дает именительный падеж
                .uppercase()

            val currency = sharedPref.getCurrency()

            // 2. Обновляем состояние Glance
            val manager = GlanceAppWidgetManager(applicationContext)
            val ids = manager.getGlanceIds(CostacWidget::class.java)

            ids.forEach { id ->
                updateAppWidgetState(applicationContext, id) { prefs ->
                    prefs[CostacWidget.CostacWidgetKeys.TOTAL_AMOUNT] = total
                    prefs[CostacWidget.CostacWidgetKeys.MONTH_NAME] = "За $monthName"
                    prefs[CostacWidget.CostacWidgetKeys.CURRENCY_SYMBOL] = currency.first
                }
                CostacWidget().update(applicationContext, id)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}