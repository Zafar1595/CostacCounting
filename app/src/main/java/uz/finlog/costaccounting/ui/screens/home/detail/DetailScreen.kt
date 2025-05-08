package uz.finlog.costaccounting.ui.screens.home.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import uz.finlog.costaccounting.ui.ScreenRoute
import uz.finlog.costaccounting.util.getDateString
import uz.finlog.costaccounting.util.toDate
import uz.finlog.costaccounting.util.AppConstants.selectedCurrency
import uz.finlog.costaccounting.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    expenseId: Int,
    navController: NavController
) {
    val expense by viewModel.selectedExpense.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val titleText = stringResource(R.string.expense_detail_title)
    val backText = stringResource(R.string.back)
    val editText = stringResource(R.string.edit)
    val deleteText = stringResource(R.string.delete)
    val labelTitle = stringResource(R.string.title)
    val labelAmount = stringResource(R.string.amount)
    val labelDate = stringResource(R.string.date)
    val labelComment = stringResource(R.string.comment)
    val noComment = stringResource(R.string.no_comment)
    val deleteDialogTitle = stringResource(R.string.delete_expense_title)
    val deleteDialogText = stringResource(R.string.delete_expense_text)
    val cancelText = stringResource(R.string.cancel)

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = backText)
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = {
                            expense?.let {
                                navController.navigate(ScreenRoute.Edit.routeWithId(it.id)) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = editText)
                        }

                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = deleteText)
                        }
                    }
                }
            )
        }
    ) { padding ->
        expense?.let { exp ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(labelTitle, style = MaterialTheme.typography.labelMedium)
                        Text(exp.title, style = MaterialTheme.typography.titleLarge)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(labelAmount, style = MaterialTheme.typography.labelMedium)
                        Text("${exp.amount} $selectedCurrency", style = MaterialTheme.typography.titleLarge)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(labelDate, style = MaterialTheme.typography.labelMedium)
                        Text("${exp.date.getDateString()} ${exp.date.toDate()}", style = MaterialTheme.typography.titleLarge)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier
                        .padding(20.dp)
                    ) {
                        Text(labelComment, style = MaterialTheme.typography.labelMedium)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp, max = 200.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = if (exp.comment.isNotBlank()) exp.comment else noComment,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(deleteDialogTitle) },
                text = { Text(deleteDialogText) },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteExpense(expense!!)
                        navController.popBackStack()
                    }) {
                        Text(deleteText)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(cancelText)
                    }
                }
            )
        }
    }
}