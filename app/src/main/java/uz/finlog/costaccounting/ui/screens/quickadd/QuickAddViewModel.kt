package uz.finlog.costaccounting.ui.screens.quickadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.entity.Expense

class QuickAddViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {
                _categories.value = it
            }
        }
    }

    fun saveExpense(amount: Double, categoryId: Int, comment: String = "") {
        viewModelScope.launch {
            val expense = Expense(
                id = 0, // Auto-generated
                title = "Quick Add", // Could be category name
                comment = comment,
                amount = amount,
                date = System.currentTimeMillis(),
                categoryId = categoryId
            )
            expenseRepository.insert(expense)
            _saveSuccess.emit(Unit)
        }
    }
}