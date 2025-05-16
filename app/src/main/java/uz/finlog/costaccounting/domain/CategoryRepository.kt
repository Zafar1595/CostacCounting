package uz.finlog.costaccounting.domain

import kotlinx.coroutines.flow.Flow
import uz.finlog.costaccounting.data.entity.CategoryEntity
import uz.finlog.costaccounting.entity.Category

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun insert(category: Category)
    suspend fun delete(category: Category)
    suspend fun insertAll(categories: List<CategoryEntity>)
    suspend fun deleteAll()
}