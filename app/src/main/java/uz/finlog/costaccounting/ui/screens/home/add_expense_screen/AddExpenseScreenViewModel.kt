package uz.finlog.costaccounting.ui.screens.home.add_expense_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.dao.ExpenseDao
import uz.finlog.costaccounting.data.entity.ExpenseEntity
import uz.finlog.costaccounting.data.entity.toExpense
import uz.finlog.costaccounting.data.entity.toExpenseEntity
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense

class AddExpenseScreenViewModel(private val repository: ExpenseRepository) : ViewModel(){

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
            _messageFlow.emit("Расход добавлен")

//            repository.insertAll(getTestData().map { it.toExpense() })
        }
    }

    fun getTestData() = listOf(
        ExpenseEntity(id = 122222, title = "Аптека", amount = 487.82, date = 1741219200000L),
        ExpenseEntity(id = 222222, title = "Аптека", amount = 217.84, date = 1741219200000L),
        ExpenseEntity(id = 3, title = "Супермаркет", amount = 414.77, date = 1741305600000L),
        ExpenseEntity(id = 4, title = "Кафе", amount = 329.62, date = 1741305600000L),
        ExpenseEntity(id = 5, title = "Еда", amount = 420.77, date = 1741392000000L),
        ExpenseEntity(id = 6, title = "Транспорт", amount = 308.45, date = 1741392000000L),
        ExpenseEntity(id = 7, title = "Развлечения", amount = 155.4, date = 1741478400000L),
        ExpenseEntity(id = 8, title = "Развлечения", amount = 210.24, date = 1741478400000L),
        ExpenseEntity(id = 9, title = "Транспорт", amount = 50.51, date = 1741564800000L),
        ExpenseEntity(id = 10, title = "Супермаркет", amount = 114.02, date = 1741564800000L),
        ExpenseEntity(id = 11, title = "Транспорт", amount = 82.24, date = 1741651200000L),
        ExpenseEntity(id = 12, title = "Еда", amount = 403.49, date = 1741651200000L),
        ExpenseEntity(id = 13, title = "Супермаркет", amount = 330.62, date = 1741737600000L),
        ExpenseEntity(id = 14, title = "Кафе", amount = 284.21, date = 1741737600000L),
        ExpenseEntity(id = 15, title = "Еда", amount = 211.55, date = 1741824000000L),
        ExpenseEntity(id = 16, title = "Транспорт", amount = 488.03, date = 1741824000000L),
        ExpenseEntity(id = 17, title = "Кафе", amount = 357.88, date = 1741910400000L),
        ExpenseEntity(id = 18, title = "Кафе", amount = 493.58, date = 1741910400000L),
        ExpenseEntity(id = 19, title = "Еда", amount = 336.8, date = 1741996800000L),
        ExpenseEntity(id = 20, title = "Еда", amount = 66.35, date = 1741996800000L),
        ExpenseEntity(id = 21, title = "Еда", amount = 407.06, date = 1742083200000L),
        ExpenseEntity(id = 22, title = "Супермаркет", amount = 93.44, date = 1742083200000L),
        ExpenseEntity(id = 23, title = "Развлечения", amount = 194.5, date = 1742169600000L),
        ExpenseEntity(id = 24, title = "Кафе", amount = 203.59, date = 1742169600000L),
        ExpenseEntity(id = 25, title = "Аптека", amount = 181.43, date = 1742256000000L),
        ExpenseEntity(id = 26, title = "Еда", amount = 314.24, date = 1742256000000L),
        ExpenseEntity(id = 27, title = "Супермаркет", amount = 165.09, date = 1742342400000L),
        ExpenseEntity(id = 28, title = "Кафе", amount = 451.77, date = 1742342400000L),
        ExpenseEntity(id = 29, title = "Супермаркет", amount = 369.17, date = 1742428800000L),
        ExpenseEntity(id = 30, title = "Супермаркет", amount = 143.84, date = 1742428800000L),
        ExpenseEntity(id = 31, title = "Еда", amount = 211.14, date = 1742515200000L),
        ExpenseEntity(id = 32, title = "Еда", amount = 53.77, date = 1742515200000L),
        ExpenseEntity(id = 33, title = "Еда", amount = 426.68, date = 1742601600000L),
        ExpenseEntity(id = 34, title = "Еда", amount = 435.85, date = 1742601600000L),
        ExpenseEntity(id = 35, title = "Кафе", amount = 484.67, date = 1742688000000L),
        ExpenseEntity(id = 36, title = "Кафе", amount = 258.74, date = 1742688000000L),
        ExpenseEntity(id = 37, title = "Супермаркет", amount = 259.85, date = 1742774400000L),
        ExpenseEntity(id = 38, title = "Аптека", amount = 380.02, date = 1742774400000L),
        ExpenseEntity(id = 39, title = "Аптека", amount = 191.99, date = 1742860800000L),
        ExpenseEntity(id = 40, title = "Кафе", amount = 222.02, date = 1742860800000L),
        ExpenseEntity(id = 41, title = "Еда", amount = 133.66, date = 1742947200000L),
        ExpenseEntity(id = 42, title = "Еда", amount = 140.76, date = 1742947200000L),
        ExpenseEntity(id = 43, title = "Кафе", amount = 273.11, date = 1743033600000L),
        ExpenseEntity(id = 44, title = "Развлечения", amount = 289.18, date = 1743033600000L),
        ExpenseEntity(id = 45, title = "Транспорт", amount = 483.94, date = 1743120000000L),
        ExpenseEntity(id = 46, title = "Развлечения", amount = 212.57, date = 1743120000000L),
        ExpenseEntity(id = 47, title = "Кафе", amount = 215.05, date = 1743206400000L),
        ExpenseEntity(id = 48, title = "Кафе", amount = 282.77, date = 1743206400000L),
        ExpenseEntity(id = 49, title = "Аптека", amount = 51.62, date = 1743292800000L),
        ExpenseEntity(id = 50, title = "Еда", amount = 198.08, date = 1743292800000L),
        ExpenseEntity(id = 51, title = "Супермаркет", amount = 324.63, date = 1743379200000L),
        ExpenseEntity(id = 52, title = "Еда", amount = 99.63, date = 1743379200000L),
        ExpenseEntity(id = 53, title = "Развлечения", amount = 139.94, date = 1743465600000L),
        ExpenseEntity(id = 54, title = "Кафе", amount = 138.98, date = 1743465600000L),
        ExpenseEntity(id = 55, title = "Развлечения", amount = 81.74, date = 1743552000000L),
        ExpenseEntity(id = 56, title = "Кафе", amount = 211.99, date = 1743552000000L),
        ExpenseEntity(id = 57, title = "Супермаркет", amount = 187.91, date = 1743638400000L),
        ExpenseEntity(id = 58, title = "Кафе", amount = 228.35, date = 1743638400000L),
        ExpenseEntity(id = 59, title = "Еда", amount = 431.62, date = 1743724800000L),
        ExpenseEntity(id = 60, title = "Еда", amount = 102.41, date = 1743724800000L),
        ExpenseEntity(id = 61, title = "Развлечения", amount = 275.36, date = 1743811200000L),
        ExpenseEntity(id = 62, title = "Еда", amount = 303.0, date = 1743811200000L),
        ExpenseEntity(id = 63, title = "Еда", amount = 270.98, date = 1743897600000L),
        ExpenseEntity(id = 64, title = "Супермаркет", amount = 382.48, date = 1743897600000L),
        ExpenseEntity(id = 65, title = "Транспорт", amount = 139.44, date = 1743984000000L),
        ExpenseEntity(id = 66, title = "Супермаркет", amount = 319.28, date = 1743984000000L),
        ExpenseEntity(id = 67, title = "Развлечения", amount = 332.07, date = 1744070400000L),
        ExpenseEntity(id = 68, title = "Транспорт", amount = 232.23, date = 1744070400000L),
        ExpenseEntity(id = 69, title = "Еда", amount = 439.53, date = 1744156800000L),
        ExpenseEntity(id = 70, title = "Супермаркет", amount = 343.69, date = 1744156800000L),
        ExpenseEntity(id = 71, title = "Транспорт", amount = 51.79, date = 1744243200000L),
        ExpenseEntity(id = 72, title = "Еда", amount = 244.83, date = 1744243200000L),
        ExpenseEntity(id = 73, title = "Развлечения", amount = 202.13, date = 1744329600000L),
        ExpenseEntity(id = 74, title = "Кафе", amount = 215.8, date = 1744329600000L),
        ExpenseEntity(id = 75, title = "Аптека", amount = 77.85, date = 1744416000000L),
        ExpenseEntity(id = 76, title = "Еда", amount = 171.49, date = 1744416000000L),
        ExpenseEntity(id = 77, title = "Аптека", amount = 193.77, date = 1744502400000L),
        ExpenseEntity(id = 78, title = "Развлечения", amount = 470.93, date = 1744502400000L),
        ExpenseEntity(id = 79, title = "Еда", amount = 337.43, date = 1744588800000L),
        ExpenseEntity(id = 80, title = "Супермаркет", amount = 257.77, date = 1744588800000L),
        ExpenseEntity(id = 81, title = "Супермаркет", amount = 468.99, date = 1744675200000L),
        ExpenseEntity(id = 82, title = "Супермаркет", amount = 364.8, date = 1744675200000L),
        ExpenseEntity(id = 83, title = "Транспорт", amount = 264.15, date = 1744761600000L),
        ExpenseEntity(id = 84, title = "Аптека", amount = 238.57, date = 1744761600000L),
        ExpenseEntity(id = 85, title = "Аптека", amount = 213.83, date = 1744848000000L),
        ExpenseEntity(id = 86, title = "Кафе", amount = 164.86, date = 1744848000000L),
        ExpenseEntity(id = 87, title = "Еда", amount = 58.77, date = 1744934400000L),
        ExpenseEntity(id = 88, title = "Транспорт", amount = 498.15, date = 1744934400000L),
        ExpenseEntity(id = 89, title = "Супермаркет", amount = 226.68, date = 1745020800000L),
        ExpenseEntity(id = 90, title = "Еда", amount = 262.97, date = 1745020800000L),
        ExpenseEntity(id = 91, title = "Еда", amount = 346.53, date = 1745107200000L),
        ExpenseEntity(id = 92, title = "Развлечения", amount = 277.61, date = 1745107200000L),
        ExpenseEntity(id = 93, title = "Транспорт", amount = 324.69, date = 1745193600000L),
        ExpenseEntity(id = 94, title = "Супермаркет", amount = 178.99, date = 1745193600000L),
        ExpenseEntity(id = 95, title = "Транспорт", amount = 69.14, date = 1745280000000L),
        ExpenseEntity(id = 96, title = "Транспорт", amount = 160.6, date = 1745280000000L),
        ExpenseEntity(id = 97, title = "Аптека", amount = 137.74, date = 1745366400000L),
        ExpenseEntity(id = 98, title = "Еда", amount = 241.6, date = 1745366400000L),
        ExpenseEntity(id = 99, title = "Еда", amount = 359.35, date = 1745452800000L),
        ExpenseEntity(id = 100, title = "Развлечения", amount = 75.64, date = 1745452800000L),
        ExpenseEntity(id = 101, title = "Еда", amount = 385.02, date = 1745539200000L),
        ExpenseEntity(id = 102, title = "Аптека", amount = 74.81, date = 1745539200000L),
        ExpenseEntity(id = 103, title = "Еда", amount = 343.62, date = 1745625600000L),
        ExpenseEntity(id = 104, title = "Еда", amount = 493.85, date = 1745625600000L),
        ExpenseEntity(id = 105, title = "Еда", amount = 467.74, date = 1745712000000L),
        ExpenseEntity(id = 106, title = "Развлечения", amount = 135.69, date = 1745712000000L),
        ExpenseEntity(id = 107, title = "Аптека", amount = 132.94, date = 1745798400000L),
        ExpenseEntity(id = 108, title = "Развлечения", amount = 328.07, date = 1745798400000L),
        ExpenseEntity(id = 109, title = "Супермаркет", amount = 320.33, date = 1745884800000L),
        ExpenseEntity(id = 110, title = "Кафе", amount = 59.63, date = 1745884800000L),
        ExpenseEntity(id = 111, title = "Супермаркет", amount = 444.16, date = 1745971200000L),
        ExpenseEntity(id = 112, title = "Супермаркет", amount = 134.71, date = 1745971200000L),
        ExpenseEntity(id = 113, title = "Кафе", amount = 422.76, date = 1746057600000L),
        ExpenseEntity(id = 114, title = "Транспорт", amount = 114.75, date = 1746057600000L),
        ExpenseEntity(id = 115, title = "Кафе", amount = 450.91, date = 1746144000000L),
        ExpenseEntity(id = 116, title = "Аптека", amount = 414.1, date = 1746144000000L),
        ExpenseEntity(id = 117, title = "Супермаркет", amount = 243.35, date = 1746230400000L),
        ExpenseEntity(id = 118, title = "Транспорт", amount = 442.24, date = 1746230400000L),
        ExpenseEntity(id = 119, title = "Кафе", amount = 451.68, date = 1746316800000L),
        ExpenseEntity(id = 120, title = "Еда", amount = 418.14, date = 1746316800000L)
    )
}