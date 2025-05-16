package uz.finlog.costaccounting.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.data.entity.CategoryEntity
import uz.finlog.costaccounting.data.entity.ExpenseEntity
import uz.finlog.costaccounting.data.entity.toEntity
import uz.finlog.costaccounting.entity.Category

@Database(entities = [ExpenseEntity::class, CategoryEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao


    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Добавляем новую таблицу categories
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                image TEXT NOT NULL
            )
        """.trimIndent()
                )

                // 2. Добавляем новое поле в таблицу expenses
                database.execSQL(
                    """
            ALTER TABLE expenses ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 0
        """.trimIndent()
                )
            }
        }

        fun create(context: Context, prefs: UserPreferences): AppDatabase {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "expense_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                insertDefaultCategoriesIfNeeded(prefs, db.categoryDao())
            }
            return db
        }

        suspend fun insertDefaultCategoriesIfNeeded(
            prefs: UserPreferences,
            dao: CategoryDao
        ) {
            if (prefs.isFirstLaunch()) {
                val default = listOf(
                    Category(id = 0, name = "Другое", image = "❓"),
                    Category(id = 1, name = "Еда", image = "🍔"),
                    Category(id = 2, name = "Транспорт", image = "🚗"),
                    Category(id = 3, name = "Здоровье", image = "💊"),
                    Category(id = 4, name = "Развлечения", image = "🎮"),
                    Category(id = 5, name = "Покупки", image = "🛍️"),
                )
                default.forEach {
                    dao.insert(it.toEntity())
                }

                prefs.setFirstLaunchDone()
            }
        }
    }
}