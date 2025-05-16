package uz.finlog.costaccounting.ui.screens.home.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.entity.Expense

class DetailViewModel(
    private val repository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    val selectedExpense = MutableStateFlow<Expense?>(null)

    fun loadExpense(id: Int) {
        viewModelScope.launch {
            selectedExpense.value = repository.getExpenseById(id)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    val categories: StateFlow<List<Category>> =
        categoryRepository.getAllCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}