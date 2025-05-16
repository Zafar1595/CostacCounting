package uz.finlog.costaccounting.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.finlog.costaccounting.data.dao.CategoryDao
import uz.finlog.costaccounting.data.entity.CategoryEntity
import uz.finlog.costaccounting.data.entity.toDomain
import uz.finlog.costaccounting.data.entity.toEntity
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.entity.Category

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {


    override fun getAllCategories(): Flow<List<Category>> =
        dao.getAllCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun insert(category: Category) {
        dao.insert(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        // Понадобится ID для удаления, получим Entity с ID = 0 (или храним ID в домене)
        dao.delete(category.toEntity()) // ⚠️ Работает корректно, если ID был сохранён
    }

    override suspend fun insertAll(categories: List<CategoryEntity>) {
        categories.forEach { dao.insert(it) }
    }

    override suspend fun deleteAll() {
        dao.clearAll()
    }
}