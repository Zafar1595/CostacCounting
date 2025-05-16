package uz.finlog.costaccounting.data


import android.content.Context
import androidx.core.content.edit

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENCY_SYMBOL = "currency_symbol"
        private const val KEY_CURRENCY_NAME = "currency_name"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    fun getCurrency(): Pair<String, String> {
        val symbol = prefs.getString(KEY_CURRENCY_SYMBOL, "$") ?: "$"
        val name = prefs.getString(KEY_CURRENCY_NAME, "Доллар США") ?: "Доллар США"
        return Pair(symbol, name)
    }

    fun setCurrency(currency: Pair<String, String>) {
        prefs.edit {
            putString(KEY_CURRENCY_SYMBOL, currency.first)
                .putString(KEY_CURRENCY_NAME, currency.second)
        }
    }

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchDone() {
        prefs.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
    }
}