package uz.finlog.costaccounting.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.finlog.costaccounting.data.dao.ExpenseDao
import uz.finlog.costaccounting.data.entity.ExpenseEntity
import uz.finlog.costaccounting.data.entity.toExpense
import uz.finlog.costaccounting.data.entity.toExpenseEntity
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.util.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class ExpenseRepositoryImpl(private val dao: ExpenseDao) : ExpenseRepository {
    override fun getAllExpenses(): Flow<List<Expense>> =
        dao.getAll().map { list -> list.map { it.toExpense() } }

    /**
     * Возвращает только те траты, которые были сделаны в указанном месяце.
     * Все траты за день суммированы
     */
    override suspend fun getExpensesForMonth(month: YearMonth): List<Expense> =
        getDailyExpensesForMonth(month, dao.getBetweenDates().map { it.toExpense() })

    /**
     * Возвращает только те траты, которые были сделаны в указанном месяце.
     */
    private fun getDailyExpensesForMonth(month: YearMonth, expenses: List<Expense>): List<Expense> {
        return expenses
            .filter { expense ->
                val expenseDate = Instant.ofEpochMilli(expense.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                YearMonth.from(expenseDate) == month
            }
            .groupBy { expense ->
                Instant.ofEpochMilli(expense.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            .map { (date, expensesForDay) ->
                Expense(
                    id = 0,
                    title = date.dayOfMonth.toString(),
                    comment = expensesForDay.joinToString { it.comment },
                    amount = expensesForDay.sumOf { it.amount },
                    date = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    categoryId = 0
                )
            }
            .sortedBy { it.date }
    }

    override suspend fun getExpensesForToday(): Pair<Double, Double> =
        calculateTodayAndMonthSums(dao.getBetweenDates().map { it.toExpense() })

    /**
     * Возвращает только те траты, которые были сделаны сегодня
     */
    private fun calculateTodayAndMonthSums(expenses: List<Expense>): Pair<Double, Double> {
        val today = LocalDate.now()
        val currentMonth = YearMonth.from(today)

        var todaySum = 0.0
        var monthSum = 0.0

        for (expense in expenses) {
            val expenseDate = Instant.ofEpochMilli(expense.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            if (expenseDate == today) {
                todaySum += expense.amount
            }
            if (YearMonth.from(expenseDate) == currentMonth) {
                monthSum += expense.amount
            }
        }

        return todaySum to monthSum
    }

    override suspend fun insert(expense: Expense) {
        dao.insert(expense.toExpenseEntity())
    }

    override suspend fun insertAll(expenses: List<Expense>) {
        dao.insertAll(expenses.map { it.toExpenseEntity() })
    }

    override suspend fun deleteAll() {
        dao.clearAll()
    }

    override suspend fun getExpenseById(expenseId: Int): Expense? =
        dao.getExpenseById(expenseId = expenseId)?.toExpense()

    /**
     * Удаляет трату из базы данных
     */
    override suspend fun deleteExpense(expense: Expense) = dao.delete(expense.toExpenseEntity())

    override suspend fun updateExpense(expense: Expense) = dao.update(expense.toExpenseEntity())

    override suspend fun getTotalSpentSince(timestamp: Long): Double = dao.getTotalSpentSince(timestamp) ?: 0.0

}