package uz.finlog.costaccounting.entity

data class Expense (
    val id: Int,
    val title: String,
    val amount: Double,
    val date: Long
)