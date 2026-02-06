package uz.finlog.costaccounting.ui.screens.home.add_expense_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.entity.ExpenseEntity
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.ui.widget.WidgetManager

class AddExpenseScreenViewModel(
    private val repository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val widgetManager: WidgetManager
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect {
                _categories.value = it
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
//            repository.insertAll(getTestData().map { it.toExpense() })
//            _messageFlow.emit("Расход добавлен")
            widgetManager.refreshWidget()
        }
    }

    fun showMessage(message: String) {
        viewModelScope.launch {
            _messageFlow.emit(message)
        }
    }

    fun getTestData() = listOf(
        ExpenseEntity(
            id = 1,
            title = "Кафе",
            amount = 50.0,
            comment = "Обед в кафе",
            date = 1741441128057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 2,
            title = "Транспорт",
            amount = 20.0,
            comment = "Проезд на автобусе",
            date = 1741441128057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 3,
            title = "Кафе",
            amount = 50.1,
            comment = "Обед в кафе",
            date = 1741527528057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 4,
            title = "Транспорт",
            amount = 20.05,
            comment = "Проезд на автобусе",
            date = 1741527528057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 5,
            title = "Кафе",
            amount = 50.2,
            comment = "Обед в кафе",
            date = 1741613928057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 6,
            title = "Транспорт",
            amount = 20.1,
            comment = "Проезд на автобусе",
            date = 1741613928057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 7,
            title = "Кафе",
            amount = 50.3,
            comment = "Обед в кафе",
            date = 1741700328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 8,
            title = "Транспорт",
            amount = 20.15,
            comment = "Проезд на автобусе",
            date = 1741700328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 9,
            title = "Кафе",
            amount = 50.4,
            comment = "Обед в кафе",
            date = 1741786728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 10,
            title = "Транспорт",
            amount = 20.2,
            comment = "Проезд на автобусе",
            date = 1741786728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 11,
            title = "Кафе",
            amount = 50.5,
            comment = "Обед в кафе",
            date = 1741873128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 12,
            title = "Транспорт",
            amount = 20.25,
            comment = "Проезд на автобусе",
            date = 1741873128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 13,
            title = "Кафе",
            amount = 50.6,
            comment = "Обед в кафе",
            date = 1741959528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 14,
            title = "Транспорт",
            amount = 20.3,
            comment = "Проезд на автобусе",
            date = 1741959528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 15,
            title = "Кафе",
            amount = 50.7,
            comment = "Обед в кафе",
            date = 1742045928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 16,
            title = "Транспорт",
            amount = 20.35,
            comment = "Проезд на автобусе",
            date = 1742045928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 17,
            title = "Кафе",
            amount = 50.8,
            comment = "Обед в кафе",
            date = 1742132328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 18,
            title = "Транспорт",
            amount = 20.4,
            comment = "Проезд на автобусе",
            date = 1742132328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 19,
            title = "Кафе",
            amount = 50.9,
            comment = "Обед в кафе",
            date = 1742218728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 20,
            title = "Транспорт",
            amount = 20.45,
            comment = "Проезд на автобусе",
            date = 1742218728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 21,
            title = "Кафе",
            amount = 51.0,
            comment = "Обед в кафе",
            date = 1742305128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 22,
            title = "Транспорт",
            amount = 20.5,
            comment = "Проезд на автобусе",
            date = 1742305128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 23,
            title = "Кафе",
            amount = 51.1,
            comment = "Обед в кафе",
            date = 1742391528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 24,
            title = "Транспорт",
            amount = 20.55,
            comment = "Проезд на автобусе",
            date = 1742391528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 25,
            title = "Кафе",
            amount = 51.2,
            comment = "Обед в кафе",
            date = 1742477928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 26,
            title = "Транспорт",
            amount = 20.6,
            comment = "Проезд на автобусе",
            date = 1742477928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 27,
            title = "Кафе",
            amount = 51.3,
            comment = "Обед в кафе",
            date = 1742564328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 28,
            title = "Транспорт",
            amount = 20.65,
            comment = "Проезд на автобусе",
            date = 1742564328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 29,
            title = "Кафе",
            amount = 51.4,
            comment = "Обед в кафе",
            date = 1742650728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 30,
            title = "Транспорт",
            amount = 20.7,
            comment = "Проезд на автобусе",
            date = 1742650728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 31,
            title = "Кафе",
            amount = 51.5,
            comment = "Обед в кафе",
            date = 1742737128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 32,
            title = "Транспорт",
            amount = 20.75,
            comment = "Проезд на автобусе",
            date = 1742737128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 33,
            title = "Кафе",
            amount = 51.6,
            comment = "Обед в кафе",
            date = 1742823528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 34,
            title = "Транспорт",
            amount = 20.8,
            comment = "Проезд на автобусе",
            date = 1742823528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 35,
            title = "Кафе",
            amount = 51.7,
            comment = "Обед в кафе",
            date = 1742909928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 36,
            title = "Транспорт",
            amount = 20.85,
            comment = "Проезд на автобусе",
            date = 1742909928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 37,
            title = "Кафе",
            amount = 51.8,
            comment = "Обед в кафе",
            date = 1742996328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 38,
            title = "Транспорт",
            amount = 20.9,
            comment = "Проезд на автобусе",
            date = 1742996328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 39,
            title = "Кафе",
            amount = 51.9,
            comment = "Обед в кафе",
            date = 1743082728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 40,
            title = "Транспорт",
            amount = 20.95,
            comment = "Проезд на автобусе",
            date = 1743082728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 41,
            title = "Кафе",
            amount = 52.0,
            comment = "Обед в кафе",
            date = 1743169128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 42,
            title = "Транспорт",
            amount = 21.0,
            comment = "Проезд на автобусе",
            date = 1743169128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 43,
            title = "Кафе",
            amount = 52.1,
            comment = "Обед в кафе",
            date = 1743255528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 44,
            title = "Транспорт",
            amount = 21.05,
            comment = "Проезд на автобусе",
            date = 1743255528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 45,
            title = "Кафе",
            amount = 52.2,
            comment = "Обед в кафе",
            date = 1743341928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 46,
            title = "Транспорт",
            amount = 21.1,
            comment = "Проезд на автобусе",
            date = 1743341928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 47,
            title = "Кафе",
            amount = 52.3,
            comment = "Обед в кафе",
            date = 1743428328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 48,
            title = "Транспорт",
            amount = 21.15,
            comment = "Проезд на автобусе",
            date = 1743428328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 49,
            title = "Кафе",
            amount = 52.4,
            comment = "Обед в кафе",
            date = 1743514728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 50,
            title = "Транспорт",
            amount = 21.2,
            comment = "Проезд на автобусе",
            date = 1743514728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 51,
            title = "Кафе",
            amount = 52.5,
            comment = "Обед в кафе",
            date = 1743601128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 52,
            title = "Транспорт",
            amount = 21.25,
            comment = "Проезд на автобусе",
            date = 1743601128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 53,
            title = "Кафе",
            amount = 52.6,
            comment = "Обед в кафе",
            date = 1743687528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 54,
            title = "Транспорт",
            amount = 21.3,
            comment = "Проезд на автобусе",
            date = 1743687528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 55,
            title = "Кафе",
            amount = 52.7,
            comment = "Обед в кафе",
            date = 1743773928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 56,
            title = "Транспорт",
            amount = 21.35,
            comment = "Проезд на автобусе",
            date = 1743773928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 57,
            title = "Кафе",
            amount = 52.8,
            comment = "Обед в кафе",
            date = 1743860328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 58,
            title = "Транспорт",
            amount = 21.4,
            comment = "Проезд на автобусе",
            date = 1743860328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 59,
            title = "Кафе",
            amount = 52.9,
            comment = "Обед в кафе",
            date = 1743946728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 60,
            title = "Транспорт",
            amount = 21.45,
            comment = "Проезд на автобусе",
            date = 1743946728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 61,
            title = "Кафе",
            amount = 53.0,
            comment = "Обед в кафе",
            date = 1744033128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 62,
            title = "Транспорт",
            amount = 21.5,
            comment = "Проезд на автобусе",
            date = 1744033128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 63,
            title = "Кафе",
            amount = 53.1,
            comment = "Обед в кафе",
            date = 1744119528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 64,
            title = "Транспорт",
            amount = 21.55,
            comment = "Проезд на автобусе",
            date = 1744119528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 65,
            title = "Кафе",
            amount = 53.2,
            comment = "Обед в кафе",
            date = 1744205928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 66,
            title = "Транспорт",
            amount = 21.6,
            comment = "Проезд на автобусе",
            date = 1744205928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 67,
            title = "Кафе",
            amount = 53.3,
            comment = "Обед в кафе",
            date = 1744292328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 68,
            title = "Транспорт",
            amount = 21.65,
            comment = "Проезд на автобусе",
            date = 1744292328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 69,
            title = "Кафе",
            amount = 53.4,
            comment = "Обед в кафе",
            date = 1744378728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 70,
            title = "Транспорт",
            amount = 21.7,
            comment = "Проезд на автобусе",
            date = 1744378728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 71,
            title = "Кафе",
            amount = 53.5,
            comment = "Обед в кафе",
            date = 1744465128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 72,
            title = "Транспорт",
            amount = 21.75,
            comment = "Проезд на автобусе",
            date = 1744465128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 73,
            title = "Кафе",
            amount = 53.6,
            comment = "Обед в кафе",
            date = 1744551528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 74,
            title = "Транспорт",
            amount = 21.8,
            comment = "Проезд на автобусе",
            date = 1744551528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 75,
            title = "Кафе",
            amount = 53.7,
            comment = "Обед в кафе",
            date = 1744637928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 76,
            title = "Транспорт",
            amount = 21.85,
            comment = "Проезд на автобусе",
            date = 1744637928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 77,
            title = "Кафе",
            amount = 53.8,
            comment = "Обед в кафе",
            date = 1744724328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 78,
            title = "Транспорт",
            amount = 21.9,
            comment = "Проезд на автобусе",
            date = 1744724328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 79,
            title = "Кафе",
            amount = 53.9,
            comment = "Обед в кафе",
            date = 1744810728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 80,
            title = "Транспорт",
            amount = 21.95,
            comment = "Проезд на автобусе",
            date = 1744810728057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 81,
            title = "Кафе",
            amount = 54.0,
            comment = "Обед в кафе",
            date = 1744897128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 82,
            title = "Транспорт",
            amount = 22.0,
            comment = "Проезд на автобусе",
            date = 1744897128057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 83,
            title = "Кафе",
            amount = 54.1,
            comment = "Обед в кафе",
            date = 1744983528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 84,
            title = "Транспорт",
            amount = 22.05,
            comment = "Проезд на автобусе",
            date = 1744983528057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 85,
            title = "Кафе",
            amount = 54.2,
            comment = "Обед в кафе",
            date = 1745069928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 86,
            title = "Транспорт",
            amount = 22.1,
            comment = "Проезд на автобусе",
            date = 1745069928057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 87,
            title = "Кафе",
            amount = 54.3,
            comment = "Обед в кафе",
            date = 1745156328057,
            categoryId = 0
        ),
        ExpenseEntity(
            id = 88,
            title = "Транспорт",
            amount = 22.15,
            comment = "Проезд на автобусе",
            date = 1745156328057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 89,
            title = "Кафе",
            amount = 54.4,
            comment = "Обед в кафе",
            date = 1745242728057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 90,
            title = "Транспорт",
            amount = 22.2,
            comment = "Проезд на автобусе",
            date = 1745242728057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 91,
            title = "Кафе",
            amount = 54.5,
            comment = "Обед в кафе",
            date = 1745329128057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 92,
            title = "Транспорт",
            amount = 22.25,
            comment = "Проезд на автобусе",
            date = 1745329128057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 93,
            title = "Кафе",
            amount = 54.6,
            comment = "Обед в кафе",
            date = 1745415528057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 94,
            title = "Транспорт",
            amount = 22.3,
            comment = "Проезд на автобусе",
            date = 1745415528057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 95,
            title = "Кафе",
            amount = 54.7,
            comment = "Обед в кафе",
            date = 1745501928057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 96,
            title = "Транспорт",
            amount = 22.35,
            comment = "Проезд на автобусе",
            date = 1745501928057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 97,
            title = "Кафе",
            amount = 54.8,
            comment = "Обед в кафе",
            date = 1745588328057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 98,
            title = "Транспорт",
            amount = 22.4,
            comment = "Проезд на автобусе",
            date = 1745588328057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 99,
            title = "Кафе",
            amount = 54.9,
            comment = "Обед в кафе",
            date = 1745674728057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 100,
            title = "Транспорт",
            amount = 22.45,
            comment = "Проезд на автобусе",
            date = 1745674728057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 101,
            title = "Кафе",
            amount = 55.0,
            comment = "Обед в кафе",
            date = 1745761128057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 102,
            title = "Транспорт",
            amount = 22.5,
            comment = "Проезд на автобусе",
            date = 1745761128057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 103,
            title = "Кафе",
            amount = 55.1,
            comment = "Обед в кафе",
            date = 1745847528057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 104,
            title = "Транспорт",
            amount = 22.55,
            comment = "Проезд на автобусе",
            date = 1745847528057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 105,
            title = "Кафе",
            amount = 55.2,
            comment = "Обед в кафе",
            date = 1745933928057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 106,
            title = "Транспорт",
            amount = 22.6,
            comment = "Проезд на автобусе",
            date = 1745933928057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 107,
            title = "Кафе",
            amount = 55.3,
            comment = "Обед в кафе",
            date = 1746020328057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 108,
            title = "Транспорт",
            amount = 22.65,
            comment = "Проезд на автобусе",
            date = 1746020328057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 109,
            title = "Кафе",
            amount = 55.4,
            comment = "Обед в кафе",
            date = 1746106728057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 110,
            title = "Транспорт",
            amount = 22.7,
            comment = "Проезд на автобусе",
            date = 1746106728057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 111,
            title = "Кафе",
            amount = 55.5,
            comment = "Обед в кафе",
            date = 1746193128057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 112,
            title = "Транспорт",
            amount = 22.75,
            comment = "Проезд на автобусе",
            date = 1746193128057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 113,
            title = "Кафе",
            amount = 55.6,
            comment = "Обед в кафе",
            date = 1746279528057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 114,
            title = "Транспорт",
            amount = 22.8,
            comment = "Проезд на автобусе",
            date = 1746279528057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 115,
            title = "Кафе",
            amount = 55.7,
            comment = "Обед в кафе",
            date = 1746365928057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 116,
            title = "Транспорт",
            amount = 22.85,
            comment = "Проезд на автобусе",
            date = 1746365928057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 117,
            title = "Кафе",
            amount = 55.8,
            comment = "Обед в кафе",
            date = 1746452328057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 118,
            title = "Транспорт",
            amount = 22.9,
            comment = "Проезд на автобусе",
            date = 1746452328057,
            categoryId = 2
        ),
        ExpenseEntity(
            id = 119,
            title = "Кафе",
            amount = 55.9,
            comment = "Обед в кафе",
            date = 1746538728057,
            categoryId = 1
        ),
        ExpenseEntity(
            id = 120,
            title = "Транспорт",
            amount = 22.95,
            comment = "Проезд на автобусе",
            date = 1746538728057,
            categoryId = 2
        ),
    )
}