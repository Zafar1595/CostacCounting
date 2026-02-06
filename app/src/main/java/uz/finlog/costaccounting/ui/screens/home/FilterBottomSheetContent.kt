package uz.finlog.costaccounting.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.finlog.costaccounting.R
import uz.finlog.costaccounting.util.DateUtils.dateParse
import java.util.Date

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