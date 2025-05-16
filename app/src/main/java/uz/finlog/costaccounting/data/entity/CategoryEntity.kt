package uz.finlog.costaccounting.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.finlog.costaccounting.entity.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val image: String // может быть URL, имя ресурса или emoji-код
)


fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        image = image
    )
}

fun Category.toEntity(id: Int = 0): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        image = image
    )
}