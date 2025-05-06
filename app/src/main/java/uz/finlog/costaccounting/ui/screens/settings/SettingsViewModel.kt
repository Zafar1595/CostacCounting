package uz.finlog.costaccounting.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.util.AppConstants
import uz.finlog.costaccounting.util.AppConstants.setSelectedCurrency

class SettingsViewModel(private val prefs: UserPreferences) : ViewModel() {

    private val _selectedCurrency = MutableStateFlow(prefs.getCurrency())
    val selectedCurrency: StateFlow<Pair<String, String>> = _selectedCurrency

    fun setCurrency(currency: Pair<String, String>) {
        prefs.setCurrency(currency)
        _selectedCurrency.value = currency
        setSelectedCurrency(currency)
    }
}