package uz.finlog.costaccounting.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import uz.finlog.costaccounting.data.entity.CategoryEntity
import uz.finlog.costaccounting.entity.Expense
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class CsvManager(private val context: Context) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())

    fun exportAllToCsvStream(
        outputStream: OutputStream,
        expenses: List<Expense>,
        categories: List<CategoryEntity>
    ) {
        val writer = outputStream.bufferedWriter()

        // --- Категории ---
        writer.appendLine("# categories")
        writer.appendLine("id,name,image")
        categories.forEach {
            writer.appendLine("${it.id},${it.name},${it.image}")
        }

        writer.appendLine()

        // --- Расходы ---
        writer.appendLine("# expenses")
        writer.appendLine("id,title,comment,amount,date,categoryId")
        expenses.forEach {
            val formattedDate = dateFormatter.format(
                Instant.ofEpochMilli(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
            )
            writer.appendLine("${it.id},${it.title},${it.comment},${it.amount},$formattedDate,${it.categoryId}")
        }

        writer.flush()
        writer.close()
    }

    suspend fun importAllFromCsv(uri: Uri): Result<Pair<List<CategoryEntity>, List<Expense>>> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return Result.Failure(Exception("Файл не найден"))
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.readLines()
            reader.close()

            val categories = mutableListOf<CategoryEntity>()
            val expenses = mutableListOf<Expense>()

            var mode: String? = null // "categories" | "expenses"
            for (line in lines) {
                when {
                    line.startsWith("#") -> {
                        mode = when {
                            line.contains("categories", ignoreCase = true) -> "categories"
                            line.contains("expenses", ignoreCase = true) -> "expenses"
                            else -> null
                        }
                    }

                    line.isBlank() || line.startsWith("id") -> continue // Пропускаем заголовки

                    else -> {
                        val parts = line.split(",")
                        when (mode) {
                            "categories" -> {
                                if (parts.size >= 3) {
                                    val id = parts[0].trim().toIntOrNull() ?: continue
                                    val name = parts[1].trim()
                                    val image = parts[2].trim()
                                    categories.add(CategoryEntity(id = id, name = name, image = image))
                                }
                            }

                            "expenses" -> {
                                if (parts.size >= 6) {
                                    val id = parts[0].trim().toIntOrNull() ?: continue
                                    val title = parts[1].trim()
                                    val comment = parts[2].trim()
                                    val amount = parts[3].trim().replace(",", ".").toDoubleOrNull() ?: continue
                                    val date = try {
                                        dateFormat.parse(parts[4].trim())?.time ?: continue
                                    } catch (_: Exception) {
                                        continue
                                    }
                                    val categoryId = parts[5].trim().toIntOrNull() ?: 0

                                    expenses.add(
                                        Expense(
                                            id = id,
                                            title = title,
                                            comment = comment,
                                            amount = amount,
                                            date = date,
                                            categoryId = categoryId
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Result.Success(Pair(categories, expenses))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}