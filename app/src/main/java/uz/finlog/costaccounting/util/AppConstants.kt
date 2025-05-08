package uz.finlog.costaccounting.util

object AppConstants {
    val currencies = listOf(
        "₽" to "Российский рубль",
        "₸" to "Казахстанский тенге",
        "KGS" to "Киргизский сом",
        "TMT" to "Туркменский манат",
        "TJS" to "Таджикский сомони",
        "₴" to "Украинская гривна",
        "BYN" to "Белорусский рубль",
        "UZS" to "Узбекский сум",
        "AZN" to "Азербайджанский манат",
        "MDL" to "Молдавский лей",
        "$"  to "Доллар США",
        "€"  to "Евро",
        "£"  to "Фунт стерлингов",
        "¥"  to "Японская иена",
        "₹"  to "Индийская рупия",
        "₩"  to "Южнокорейская вона",
        "AED" to "Дирхам ОАЭ",
        "VND" to "Вьетнамский донг",
        "R$" to "Бразильский реал",
        "C$" to "Канадский доллар",
        "A$" to "Австралийский доллар"
    )

    var selectedCurrency: String = ""
    fun setSelectedCurrency(currency: Pair<String, String>) {
        selectedCurrency = currency.first
    }

    val adUnitId = "ca-app-pub-5869958442327321/6279705954" //Тестовый "ca-app-pub-3940256099942544/6300978111" (Статичный баннер)
    val adUnitId2 = "ca-app-pub-5869958442327321/5693366682" // Тестовый "ca-app-pub-3940256099942544/1033173712" (Межстраничный)
}