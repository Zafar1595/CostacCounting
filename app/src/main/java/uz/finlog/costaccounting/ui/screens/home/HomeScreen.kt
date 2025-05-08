package uz.finlog.costaccounting.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uz.finlog.costaccounting.ui.ScreenRoute
import uz.finlog.costaccounting.util.getDateString
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import uz.finlog.costaccounting.util.DateUtils.toDisplayDate
import uz.finlog.costaccounting.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.getAllexpenses()
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val expenses by viewModel.filteredExpenses.collectAsState()
    val groupedExpenses = expenses
        .sortedByDescending { it.date }
        .groupBy { it.date.toDisplayDate() }

    val title = stringResource(R.string.expenses)
    val searchHint = stringResource(R.string.search_by_title)
    val totalFilteredSum = String.format("%.2f", expenses.sumOf { it.amount })
    val totalAllTimeText =
        stringResource(R.string.total_all_time, totalFilteredSum, selectedCurrency)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        totalAllTimeText,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(bottom = 16.dp, end = 16.dp)
                            .weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text(searchHint) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                groupedExpenses.forEach { (date, expenseList) ->
                    item {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }

                    items(expenseList) { expense ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(ScreenRoute.Detail.routeWithId(expense.id)) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        "${expense.amount} $selectedCurrency",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column(
                                        verticalArrangement = Arrangement.Bottom,
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = expense.date.getDateString(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
