package uz.finlog.costaccounting.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.entity.ExpenseStatItem
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class StatsScreenViewModel(private val repository: ExpenseRepository) : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    init {
        observeAllexpenses()
    }

    private fun observeAllexpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { _expenses.value = it }
        }
    }

    private val _stats = MutableStateFlow<List<ExpenseStatItem>>(emptyList())
    val stats: StateFlow<List<ExpenseStatItem>> = _stats

    fun getStats() {
        viewModelScope.launch {


            val ex = repository.getExpensesForToday()
            val today = LocalDate.now()

            val todayTotal = expenses.value
                .filter {
                    Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault())
                        .toLocalDate() == today
                }.sumOf { it.amount }

            val monthExpenses = expenses.value
                .filter {
                    val localDate = Instant.ofEpochMilli(it.date).
                    atZone(ZoneId.systemDefault()).toLocalDate()
                    YearMonth.from(localDate) == YearMonth.now()
                }
            val monthTotal = monthExpenses.sumOf { it.amount }

            _stats.value = listOf(
                ExpenseStatItem("За сегодня", todayTotal),
                ExpenseStatItem("За этот месяц ", monthTotal)
            )
        }
    }

    private val _expensesByDay = MutableStateFlow<List<Expense>>(emptyList())
    val expensesByDay: StateFlow<List<Expense>> = _expensesByDay

    fun getExpensesForMonth(month: YearMonth) {
        viewModelScope.launch {
            _expensesByDay.value = repository.getExpensesForMonth(month)
        }
    }
}