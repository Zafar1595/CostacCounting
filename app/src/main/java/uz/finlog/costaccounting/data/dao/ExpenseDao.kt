package uz.finlog.costaccounting.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.finlog.costaccounting.data.entity.ExpenseEntity


@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<ExpenseEntity>)

//    @Query("SELECT * FROM expenses ORDER BY date DESC")
//    suspend fun getAll(): List<ExpenseEntity>

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getBetweenDates(): List<ExpenseEntity>

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}