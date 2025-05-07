package uz.finlog.costaccounting.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
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

    fun exportExpensesToCsvStream(outputStream: OutputStream, expenses: List<Expense>) {
        val writer = outputStream.bufferedWriter()
        val csvHeader = "ID,Название,Сумма,Дата"
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())

        writer.appendLine(csvHeader)
        for (expense in expenses) {
            val dateFormatted = formatter.format(
                Instant.ofEpochMilli(expense.date)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
            )
            writer.appendLine("${expense.id},${expense.title},${expense.comment},${expense.amount},$dateFormatted")
        }
        writer.flush()
        writer.close()
    }

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    suspend fun importExpensesFromCsv(uri: Uri): Result<List<Expense>> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return Result.Failure(Exception("Файл не найден"))
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.readLines()
            reader.close()

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val expenses = mutableListOf<Expense>()

            for (line in lines.drop(1)) {
                val parts = line.split(",")
                if (parts.size >= 5) {
                    val id = parts[0].trim().toIntOrNull() ?: continue
                    val title = parts[1].trim()
                    val comment = parts[2].trim()
                    val amountStr = parts[3].trim().replace(",", ".")
                    val amount = amountStr.toDoubleOrNull() ?: continue
                    val dateStr = parts[4].trim()

                    val date = try {
                        dateFormat.parse(dateStr)?.time ?: continue
                    } catch (e: Exception) {
                        continue
                    }

                    expenses.add(Expense(id, title, comment, amount, date))
                }
            }

            Result.Success(expenses)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}