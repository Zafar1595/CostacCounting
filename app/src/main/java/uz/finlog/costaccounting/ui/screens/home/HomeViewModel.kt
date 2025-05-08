package uz.finlog.costaccounting.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.dao.ExpenseDao
import uz.finlog.costaccounting.data.entity.toExpense
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense

class HomeViewModel(private val repository: ExpenseRepository) : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    fun getAllexpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { _expenses.value = it }
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    val filteredExpenses = combine(_searchQuery, expenses) { query, list ->
        if (query.isBlank()) list
        else list.filter { it.title.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}