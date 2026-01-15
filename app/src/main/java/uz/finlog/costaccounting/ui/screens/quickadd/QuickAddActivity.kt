package uz.finlog.costaccounting.ui.screens.quickadd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.ui.theme.CostAccountingTheme

class QuickAddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true) // Dismiss when clicking outside the dialog window
        setContent {
            CostAccountingTheme {
                QuickAddScreen(onFinish = { finish() })
            }
        }
    }
}

@Composable
fun QuickAddScreen(
    viewModel: QuickAddViewModel = koinViewModel(),
    onFinish: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()

    var amount by rememberSaveable { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable { mutableStateOf<Int?>(null) }
    var comment by rememberSaveable { mutableStateOf("") }

    val selectedCategory = categories.find { it.id == selectedCategoryId }

    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect {
            onFinish()
        }
    }

    // No need for Box/Scrim as the window is floating and dimmed by the OS theme
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Quick Add Expense", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comment (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Select Category", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(200.dp) // Fixed height for category grid
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = category.id == selectedCategoryId,
                        onClick = { selectedCategoryId = category.id }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onFinish() }, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        amount.toDoubleOrNull()?.let {
                            if (selectedCategory != null) {
                                viewModel.saveExpense(it, selectedCategory.id, comment)
                            }
                        }
                    },
                    enabled = amount.isNotEmpty() && selectedCategory != null
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = category.image) // Assuming image is emoji or short string. If URL, need AsyncImage
            Text(text = category.name, style = MaterialTheme.typography.bodySmall, maxLines = 1)
        }
    }
}