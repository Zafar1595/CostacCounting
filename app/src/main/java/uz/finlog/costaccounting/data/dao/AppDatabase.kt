package uz.finlog.costaccounting.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.finlog.costaccounting.data.entity.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}