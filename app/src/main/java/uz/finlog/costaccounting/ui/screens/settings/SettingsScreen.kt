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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.finlog.costaccounting.R
import uz.finlog.costaccounting.util.AppConstants.currencies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val currency by viewModel.selectedCurrency.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { viewModel.importExpenses(it) }
        }
    )

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.let { stream ->
                    viewModel.exportExpenses(stream)
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

    val settingsTitle = stringResource(R.string.settings)
    val currencyLabel = stringResource(R.string.currency)
    val resetButton = stringResource(R.string.reset_all_data)
    val exportButton = stringResource(R.string.export_csv)
    val importButton = stringResource(R.string.import_csv)

    Scaffold(
        topBar = { TopAppBar(title = { Text(settingsTitle) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = currencyLabel, style = MaterialTheme.typography.titleMedium)
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
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(resetButton)
            }

            Button(
                onClick = { showExportDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(exportButton)
            }

            Button(
                onClick = {
                    filePickerLauncher.launch(
                        arrayOf("text/comma-separated-values", "text/csv", "application/csv")
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(importButton)
            }
        }
    }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirm = {
                viewModel.clearData()
                showConfirmDialog = false
            },
            onDismiss = { showConfirmDialog = false },
            onCancel = { showConfirmDialog = false }
        )
    }

    if (showExportDialog) {
        ExportDialog(
            onSuccess = {
                exportLauncher.launch("expenses_export.csv")
                showExportDialog = false
            },
            onDismiss = { showExportDialog = false }
        )
    }
}

@Composable
private fun ExportDialog(onSuccess: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onSuccess) {
                Text(stringResource(R.string.export))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.confirm_export)) },
        text = { Text(stringResource(R.string.export_message)) }
    )
}

@Composable
fun ConfirmDialog(
    title: String = stringResource(R.string.confirmation),
    message: String = stringResource(R.string.are_you_sure),
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
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.no))
            }
        }
    )
}
