package uz.finlog.costaccounting.domain

import kotlinx.coroutines.flow.Flow
import uz.finlog.costaccounting.entity.Expense
import java.time.YearMonth

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    /**
     * Возвращает только те траты, которые были сделаны в указанном месяце.
     * Все траты за день суммированы
     */
    suspend fun getExpensesForMonth(month: YearMonth): List<Expense>
    /**
     * Возвращает только те траты, которые были сделаны сегодня
     * @return Pair<Double, Double> - Pair(todaySum, monthSum)
     */
    suspend fun getExpensesForToday(): Pair<Double, Double>
    /**
     * Добавляет трату в базу данных
     */
    suspend fun insert(expense: Expense)
    /**
     * Добавляет список трат в базу данных
     */
    suspend fun insertAll(expenses: List<Expense>)
    /**
     * Удаляет все траты из базы данных
     */
    suspend fun deleteAll()
    /**
     * Возвращает трату по id
     */
    suspend fun getExpenseById(expenseId: Int): Expense?
    /**
     * Удаляет трату из базы данных
     */
    suspend fun deleteExpense(expense: Expense)

    /**
     * Обновляем расход
     */
    suspend fun updateExpense(expense: Expense)
}