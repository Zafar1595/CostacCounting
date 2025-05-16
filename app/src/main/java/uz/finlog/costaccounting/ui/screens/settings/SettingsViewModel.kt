package uz.finlog.costaccounting.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.data.entity.toEntity
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.util.AppConstants
import uz.finlog.costaccounting.util.AppConstants.setSelectedCurrency
import uz.finlog.costaccounting.util.CsvManager
import uz.finlog.costaccounting.util.Result
import java.io.OutputStream

class SettingsViewModel(
    private val prefs: UserPreferences,
    private val repository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val csvManager: CsvManager
) : ViewModel() {

    private val _selectedCurrency = MutableStateFlow(prefs.getCurrency())
    val selectedCurrency: StateFlow<Pair<String, String>> = _selectedCurrency

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow

    fun setCurrency(currency: Pair<String, String>) {
        prefs.setCurrency(currency)
        _selectedCurrency.value = currency
        setSelectedCurrency(currency)
    }

    fun clearData() {
        viewModelScope.launch {
            repository.deleteAll()
            prefs.setCurrency(Pair("$", "Доллар США"))
            _selectedCurrency.value = Pair("$", "Доллар США")
            AppConstants.selectedCurrency = "$"
//            categoryRepository.deleteAll()
            _messageFlow.emit("Все данные удалены")
        }
    }

    fun exportAll(outputStream: OutputStream) {
        viewModelScope.launch {
            val expenses = repository.getAllExpenses().first()
            val categories = categoryRepository.getAllCategories().first()
            csvManager.exportAllToCsvStream(outputStream, expenses, categories.map { it.toEntity() })
            _messageFlow.emit("Экспорт выполнен успешно")
        }
    }

    fun importAll(uri: Uri) {
        viewModelScope.launch {
            when (val result = csvManager.importAllFromCsv(uri)) {
                is Result.Success -> {
                    val (categories, expenses) = result.getOrNull() ?: Pair(emptyList(), emptyList())

                    if (categories.isEmpty() && expenses.isEmpty()) {
                        _messageFlow.emit("Файл пустой или не содержит допустимых данных")
                        return@launch
                    }

                    categoryRepository.insertAll(categories)
                    repository.insertAll(expenses)

                    _messageFlow.emit("Импортировано: ${categories.size} категорий, ${expenses.size} расходов")
                }

                is Result.Failure -> {
                    _messageFlow.emit("Ошибка импорта: ${result.exceptionOrNull()?.localizedMessage}")
                }
            }
        }
    }


}