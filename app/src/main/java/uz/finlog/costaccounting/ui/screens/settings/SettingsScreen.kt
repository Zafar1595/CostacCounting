package uz.finlog.costaccounting.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import uz.finlog.costaccounting.util.AppConstants.currencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val currency by viewModel.selectedCurrency.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    var showExportDialog by remember { mutableStateOf(false) }

    // Выбор файла для импорта
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.importExpenses(it)
            }
        }
    )
    val context = LocalContext.current
        // Выбор места для экспорта
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri ->
            uri?.let {
                val outputStream = context.contentResolver.openOutputStream(uri)
                outputStream?.let {
                    viewModel.exportExpenses(it)
                }
            }
        }
    )

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Настройки") })
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
            Text(text = "Валюта", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("${currency.first} - ${currency.second}")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    currencies.forEach {
                        DropdownMenuItem(
                            text = { Text("${it.first} - ${it.second}") },
                            onClick = {
                                viewModel.setCurrency(it)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    showConfirmDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сбросить все данные")
            }

            Button(
                onClick = { showExportDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Экспорт в CSV")
            }

            Button(
                onClick = {
                    filePickerLauncher.launch(
                        arrayOf(
                            "text/comma-separated-values",
                            "text/csv",
                            "application/csv"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Импортировать из CSV")
            }

        }
    }

    if (showConfirmDialog)
        ConfirmDialog(
            onConfirm = {
                viewModel.clearData()
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            },
            onCancel = {
                showConfirmDialog = false

            })

    if (showExportDialog)
        ExportDialog(
            onSuccess = {
                exportLauncher.launch("expenses_export.csv")
                showExportDialog = false

            }, onDismiss = {
                showExportDialog = false
            }
        )
}

@Composable
private fun ExportDialog(onSuccess: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onSuccess) {
                Text("Экспортировать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        title = { Text("Подтвердите экспорт") },
        text = {
            Text("Данные будут экспортированы в формате CSV в папку \"Загрузки\" (Downloads). Вы уверены?")
        }
    )

}

@Composable
fun ConfirmDialog(
    title: String = "Подтверждение",
    message: String = "Вы уверены?",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Нет")
            }
        }
    )
}
