package uz.finlog.costaccounting.ui.screens.stats

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.util.AppConstants.russianMonths
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatsScreen(navController: NavController, viewModel: StatsScreenViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getStats()
        viewModel.getExpensesForMonth(YearMonth.now())
    }
    val stats by viewModel.stats.collectAsState()
    val expensesByDay by viewModel.expensesByDay.collectAsState()
    var selectedMonth = remember { mutableStateOf(YearMonth.now()) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Статистика", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            stats.forEach {
                Text(
                    "${it.text}: ${it.transactionAmount} $selectedCurrency",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        MonthSelector(selected = selectedMonth.value, onMonthSelected = {
            selectedMonth.value= it
            viewModel.getExpensesForMonth(it)
        })
        Spacer(modifier = Modifier.height(16.dp))

        StatsChart(expensesByDay)
    }
}

@Composable
fun MonthSelector(selected: YearMonth, onMonthSelected: (YearMonth) -> Unit) {
    val months = (0..11).map { YearMonth.now().minusMonths(it.toLong()) }
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = "${russianMonths[selected.monthValue - 1]} ${selected.year}",
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            months.forEach { month ->
                DropdownMenuItem(
                    text = {
                        Text("${russianMonths[month.monthValue - 1]} ${month.year}")
                    },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatsChart(expenses: List<Expense>) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    AndroidView(
        factory = {
            BarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    600
                )
                description.isEnabled = false
                setFitBars(true)
                axisRight.isEnabled = false
                legend.isEnabled = false // отключим легенду для чистоты
                setScaleEnabled(false) // отключим масштабирование
                setTouchEnabled(false) // запретить взаимодействие, если не нужно
            }
        },
        update = { chart ->
            val entries = expenses.mapIndexed { index, expense ->
                BarEntry(index.toFloat(), expense.amount.toFloat())
            }

            val labels = expenses.map { expense ->
                val date = Instant.ofEpochMilli(expense.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                "${date.dayOfMonth}/${date.monthValue}"
            }

            val dataSet = BarDataSet(entries, "Расходы").apply {
                color = colorScheme.primary.toArgb()
                valueTextColor = colorScheme.onBackground.toArgb()
                valueTextSize = 12f
            }

            val barData = BarData(dataSet)
            barData.setValueFormatter(object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.toInt()?.toString() ?: ""
                }
            })

            chart.data = barData

            // Настройка оси X (даты под колоннами)
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelRotationAngle = 90f
                textColor = colorScheme.onBackground.toArgb()
                textSize = 10f
            }

            // Настройка левой оси Y
            chart.axisLeft.apply {
                textColor = colorScheme.onBackground.toArgb()
                textSize = 10f
                gridColor = colorScheme.outline.toArgb()
            }

            chart.setBackgroundColor(colorScheme.background.toArgb())
            chart.invalidate()
        }
    )
}