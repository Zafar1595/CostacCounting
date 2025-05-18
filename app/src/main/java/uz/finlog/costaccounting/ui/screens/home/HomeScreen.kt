package uz.finlog.costaccounting.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.R
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.ui.ScreenRoute
import uz.finlog.costaccounting.util.getDateString
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import uz.finlog.costaccounting.util.DateUtils.dateParse
import uz.finlog.costaccounting.util.DateUtils.toDisplayDate
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
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

    val categories = viewModel.categories.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = {
                        coroutineScope.launch { sheetState.show() }
                    }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Фильтр")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Surface(
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 6.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = totalAllTimeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        label = { Text(searchHint) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
                        item {
                            CategoryChip(
                                name = stringResource(R.string.all_categories),
                                isSelected = selectedCategoryId == null,
                                onClick = { viewModel.onCategorySelected(null) }
                            )
                        }
                        items(categories.value) { category ->
                            CategoryChip(
                                name = category.name,
                                isSelected = selectedCategoryId == category.id,
                                onClick = { viewModel.onCategorySelected(category.id) }
                            )
                        }
                    }
                }

                groupedExpenses.forEach { (date, expenseList) ->
                    item {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }

                    items(expenseList) { expense ->
                        val interactionSource = remember { MutableInteractionSource() }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        navController.navigate(
                                            ScreenRoute.Detail.routeWithId(
                                                expense.id
                                            )
                                        ) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            ) {
                                Text(
                                    text = expense.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${expense.amount} $selectedCurrency",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = expense.date.getDateString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Show filter bottom sheet if visible
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
            },
            sheetState = sheetState
        ) {
            val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
            val startDate = remember { mutableStateOf<Long?>(null) }
            val endDate = remember { mutableStateOf<Long?>(null) }
            startDate.value = viewModel.getStartDate()
            endDate.value = viewModel.getEndDate()

            FilterBottomSheetContent(
                initialSelectedCategoryId = selectedCategoryId,
                initialStartDate = startDate.value,
                initialEndDate = endDate.value,
                onApply = { categoryId, start, end ->
                    if (start == null || end == null || start <= end) {
                        viewModel.onCategorySelected(categoryId)
                        viewModel.onDateRangeSelected(start, end)
                        coroutineScope.launch { sheetState.hide() }
                    } else {
                        coroutineScope.launch {
                            viewModel.showMessage("Дата начала не может быть позже даты окончания")
                        }
                    }
                },
                onClear = {
                    viewModel.clearFilters()
                },
                onClose = {
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetContent(
    initialSelectedCategoryId: Int?,
    initialStartDate: Long?,
    initialEndDate: Long?,
    onApply: (Int?, Long?, Long?) -> Unit,
    onClear: () -> Unit,
    onClose: () -> Unit
) {
    val selectedCategory = remember { mutableStateOf(initialSelectedCategoryId) }
    val currentStartDate = remember { mutableStateOf(initialStartDate) }
    val currentEndDate = remember { mutableStateOf(initialEndDate) }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.filter_by_date), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showStartPicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(currentStartDate.value?.let { "От: ${Date(it).time.dateParse()}" } ?: "От")
                }

                OutlinedButton(
                    onClick = { showEndPicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(currentEndDate.value?.let { "До: ${Date(it).time.dateParse()}" } ?: "До")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = {
                onClear()
                onClose()
            }) {
                Text(stringResource(R.string.clear_filters))
            }
            Button(onClick = {
                onApply(
                    selectedCategory.value,
                    currentStartDate.value,
                    currentEndDate.value
                )
                if (currentStartDate.value == null || currentEndDate.value == null || (currentStartDate.value
                        ?: 0) < (currentEndDate.value ?: 0)
                )
                    onClose()
            }) {
                Text(stringResource(R.string.apply))
            }
        }
    }

    if (showStartPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    currentStartDate.value = state.selectedDateMillis
                    showStartPicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (showEndPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    currentEndDate.value = state.selectedDateMillis
                    showEndPicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
fun CategoryChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.background
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = RoundedCornerShape(16.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
