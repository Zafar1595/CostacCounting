package uz.finlog.costaccounting.data


import android.content.Context

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENCY_SYMBOL = "currency_symbol"
        private const val KEY_CURRENCY_NAME = "currency_name"
    }

    fun getCurrency(): Pair<String, String> {
        val symbol = prefs.getString(KEY_CURRENCY_SYMBOL, "") ?: ""
        val name = prefs.getString(KEY_CURRENCY_NAME, "") ?: ""
        return Pair(symbol, name)
    }

    fun setCurrency(currency: Pair<String, String>) {
        prefs.edit()
            .putString(KEY_CURRENCY_SYMBOL, currency.first)
            .putString(KEY_CURRENCY_NAME, currency.second)
            .apply()
    }
}