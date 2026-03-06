package uz.finlog.costaccounting.ui.screens.stats

import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import uz.finlog.costaccounting.R
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import uz.finlog.costaccounting.util.formatAmount
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId

enum class StatsViewMode {
    CHART, LIST
}

@Composable
fun StatsScreen(navController: NavController, viewModel: StatsScreenViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getStats()
        viewModel.getExpensesForMonth(YearMonth.now())
    }

    val stats by viewModel.stats.collectAsState()
    val expensesByDay by viewModel.expensesByDay.collectAsState()
    val allExpenses by viewModel.expenses.collectAsState()
    val selectedMonth = remember { mutableStateOf(YearMonth.now()) }
    val statsTitle = stringResource(R.string.statistics)
    var viewMode by remember { mutableStateOf(StatsViewMode.CHART) }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 6.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = statsTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        stats.forEach {
                            Text(
                                text = "${it.text}: ${it.transactionAmount.formatAmount()} $selectedCurrency",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (stats.last() != it) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                MonthSelector(
                    selected = selectedMonth.value,
                    onMonthSelected = {
                        selectedMonth.value = it
                        viewModel.getExpensesForMonth(it)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Переключатель вида
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        onClick = { viewMode = StatsViewMode.CHART },
                        selected = viewMode == StatsViewMode.CHART
                    ) {
                        Text(stringResource(R.string.chart))
                    }
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        onClick = { viewMode = StatsViewMode.LIST },
                        selected = viewMode == StatsViewMode.LIST
                    ) {
                        Text(stringResource(R.string.list))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (viewMode == StatsViewMode.CHART) {
                    StatsChart(expensesByDay)
                }
            }

            if (viewMode == StatsViewMode.LIST) {
                MonthlyExpensesList(allExpenses)
            }
        }
    }
}

@Composable
fun MonthlyExpensesList(expenses: List<Expense>) {
    val russianMonths = stringArrayResource(R.array.russian_months)
    
    val monthlyTotals = expenses.groupBy {
        val date = Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
        YearMonth.of(date.year, date.month)
    }.mapValues { entry ->
        entry.value.sumOf { it.amount }
    }.toList().sortedByDescending { it.first }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(monthlyTotals) { (month, total) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${russianMonths[month.monthValue - 1]} ${month.year}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${total.formatAmount()} $selectedCurrency",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MonthSelector(selected: YearMonth, onMonthSelected: (YearMonth) -> Unit) {
    val months = (0..11).map { YearMonth.now().minusMonths(it.toLong()) }
    var expanded by remember { mutableStateOf(false) }
    val russianMonths = stringArrayResource(R.array.russian_months)

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("${russianMonths[selected.monthValue - 1]} ${selected.year}")
        }

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
    val chartLabel = stringResource(R.string.expenses_chart_label)

    if (expenses.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_data_for_selected_month),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
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
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setTouchEnabled(false)
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

                val dataSet = BarDataSet(entries, chartLabel).apply {
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

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    labelRotationAngle = 90f
                    textColor = colorScheme.onBackground.toArgb()
                    textSize = 10f
                }

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
}
