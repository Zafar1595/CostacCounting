package uz.finlog.costaccounting.ui.screens.home.add_expense_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uz.finlog.costaccounting.R
import uz.finlog.costaccounting.entity.Expense
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavController, viewModel: AddExpenseScreenViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = remember {
        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")
    }

    val localDate = Instant.ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.add_expense_title)) })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(id = R.string.label_title)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(id = R.string.label_amount)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(stringResource(id = R.string.label_comment)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // можно настроить под нужную высоту
                singleLine = false,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Дата
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.label_date, formatter.format(localDate)))
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            selectedDateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            showDatePicker = false
                        }) {
                            Text(stringResource(id = R.string.ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row {
                val errorFillFields = stringResource(id = R.string.error_fill_fields)
                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull()
                        if (title.isNotBlank() && amt != null) {
                            viewModel.addExpense(
                                Expense(
                                    id = 0,
                                    title = title,
                                    comment = comment,
                                    amount = amt,
                                    date = selectedDateMillis
                                )
                            )
                            navController.popBackStack()
                        } else {
                            viewModel.showMessage(message = errorFillFields)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.save))
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        }
    }
}