package uz.finlog.costaccounting.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.entity.Expense

class HomeViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    private val _startDate = MutableStateFlow<Long?>(null)
    private val _endDate = MutableStateFlow<Long?>(null)

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow

    private val _categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val categories: StateFlow<List<Category>> = _categories

    val filteredExpenses: StateFlow<List<Expense>> = combine(
        expenseRepository.getAllExpenses(),
        _searchQuery,
        _selectedCategoryId,
        _startDate,
        _endDate
    ) { expenses, query, categoryId, startDate, endDate ->
        expenses.filter { expense ->
            val matchesQuery = expense.title.contains(query, ignoreCase = true)
            val matchesCategory = categoryId == null || expense.categoryId == categoryId
            val matchesDate = (startDate == null || expense.date >= startDate) &&
                    (endDate == null || expense.date <= endDate)
            matchesQuery && matchesCategory && matchesDate
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    fun onDateRangeSelected(start: Long?, end: Long?) {
        _startDate.value = start
        _endDate.value = end

    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategoryId.value = null
        _startDate.value = null
        _endDate.value = null
    }

    fun getStartDate(): Long? = _startDate.value
    fun getEndDate(): Long? = _endDate.value

    fun showMessage(message: String){
        _messageFlow.tryEmit(message)
    }
}
