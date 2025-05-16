package uz.finlog.costaccounting.entity


data class Category(
    val id: Int,
    val name: String,
    val image: String // может быть URL, имя ресурса или emoji-код
)