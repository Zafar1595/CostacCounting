package uz.finlog.costaccounting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.finlog.costaccounting.entity.Expense

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val comment: String,
    val amount: Double,
    val date: Long,
    val categoryId: Int
)

fun ExpenseEntity.toExpense() = Expense(
    id = id,
    title = title,
    amount = amount,
    comment = comment,
    date = date,
    categoryId = categoryId
)

fun Expense.toExpenseEntity() = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    comment = comment,
    date = date,
    categoryId = categoryId
)