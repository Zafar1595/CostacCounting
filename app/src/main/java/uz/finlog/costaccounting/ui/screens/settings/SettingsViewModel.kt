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
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.util.AppConstants
import uz.finlog.costaccounting.util.AppConstants.setSelectedCurrency
import uz.finlog.costaccounting.util.CsvManager
import uz.finlog.costaccounting.util.Result
import java.io.OutputStream

class SettingsViewModel(
    private val prefs: UserPreferences,
    private val repository: ExpenseRepository,
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
            _messageFlow.emit("Все данные удалены")
        }
    }

    fun exportExpenses(outputStream: OutputStream) {
        viewModelScope.launch {
            val data = repository.getAllExpenses().first() // или другой способ получить данные
            csvManager.exportExpensesToCsvStream(outputStream, data)
            _messageFlow.emit("Экспорт выполнен успешно")
        }
    }



    fun importExpenses(uri: Uri) {
        viewModelScope.launch {
            when (val result = csvManager.importExpensesFromCsv(uri)) {
                is Result.Success-> {
                    result.getOrNull()?.let { expenses ->
                        if (expenses.isNotEmpty()) {
                            repository.insertAll(expenses)
                            _messageFlow.emit("Импортировано ${expenses.size} расходов")
                        } else {
                            _messageFlow.emit("Файл пустой или не содержит допустимых данных")
                        }
                    }
                }
                is Result.Failure -> {
                    _messageFlow.emit("Ошибка импорта: ${result.exceptionOrNull()?.localizedMessage}")
                }
            }
        }
    }


}