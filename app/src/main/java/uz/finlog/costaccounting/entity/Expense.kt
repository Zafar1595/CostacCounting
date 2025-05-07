package uz.finlog.costaccounting.entity

data class Expense (
    val id: Int,
    val title: String,
    val comment: String,
    val amount: Double,
    val date: Long
)