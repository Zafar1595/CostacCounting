package uz.finlog.costaccounting.domain

import kotlinx.coroutines.flow.Flow
import uz.finlog.costaccounting.data.entity.ExpenseEntity
import uz.finlog.costaccounting.entity.Expense
import java.time.YearMonth

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    /**
     * Возвращает только те траты, которые были сделаны в указанном месяце.
     * Все траты за день суммированы
     */
    suspend fun getExpensesForMonth(month: YearMonth): List<Expense>
    suspend fun getExpensesForToday(): Pair<Double, Double>
    suspend fun insert(expense: Expense)
    suspend fun insertAll(expenses: List<Expense>)
    suspend fun deleteAll()
}