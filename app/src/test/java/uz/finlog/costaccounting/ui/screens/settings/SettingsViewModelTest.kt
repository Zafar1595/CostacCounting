package uz.finlog.costaccounting.ui.screens.settings

import android.net.Uri
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import uz.finlog.costaccounting.util.CsvManager
import uz.finlog.costaccounting.util.Result
import java.io.OutputStream
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var repository: ExpenseRepository
    private lateinit var prefs: UserPreferences
    private lateinit var csvManager: CsvManager
    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        prefs = mockk(relaxed = true)
        csvManager = mockk(relaxed = true)

        every { prefs.getCurrency() } returns Pair("$", "Dollar")
        viewModel = SettingsViewModel(prefs, repository, csvManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setCurrency updates prefs and state flow`() {
        val newCurrency = Pair("€", "Euro")

        viewModel.setCurrency(newCurrency)

        verify { prefs.setCurrency(newCurrency) }
        assertEquals(newCurrency, viewModel.selectedCurrency.value)
    }

    @Test
    fun `clearData clears data and resets currency`() = runTest {
        coEvery { repository.deleteAll() } just Runs

        val messages = mutableListOf<String>()
        val job = launch {
            viewModel.messageFlow.collect {
                messages.add(it)
            }
        }

        viewModel.clearData()
        advanceUntilIdle()

        coVerify { repository.deleteAll() }
        verify { prefs.setCurrency(Pair("$", "Доллар США")) }
        assertEquals("Все данные удалены", messages.first())

        job.cancel()
    }

    @Test
    fun `exportExpenses calls csvManager and emits success message`() = runTest {
        val stream = mockk<OutputStream>(relaxed = true)
        val expenses = listOf(
            Expense(1, "Test", "Test", 10.0, 123L)
        )

        coEvery { repository.getAllExpenses() } returns MutableStateFlow(expenses)
        coEvery { csvManager.exportExpensesToCsvStream(stream, expenses) } just Runs

        viewModel.exportExpenses(stream)
        advanceUntilIdle()

        val msg = viewModel.messageFlow.first()
        assertEquals("Экспорт выполнен успешно", msg)
    }

    @Test
    fun `importExpenses handles successful import`() = runTest {
        val uri = mockk<Uri>()
        val importedExpenses = listOf(
            Expense(1, "Imported", "note", 15.0, 123L)
        )
        coEvery { csvManager.importExpensesFromCsv(uri) } returns Result.Success(importedExpenses)
        coEvery { repository.insertAll(importedExpenses) } just Runs

        viewModel.importExpenses(uri)
        advanceUntilIdle()

        val msg = viewModel.messageFlow.first()
        assertEquals("Импортировано 1 расходов", msg)
    }

    @Test
    fun `importExpenses handles empty import`() = runTest {
        val uri = mockk<Uri>()
        coEvery { csvManager.importExpensesFromCsv(uri) } returns Result.Success(emptyList())

        viewModel.importExpenses(uri)
        advanceUntilIdle()

        val msg = viewModel.messageFlow.first()
        assertEquals("Файл пустой или не содержит допустимых данных", msg)
    }

    @Test
    fun `importExpenses handles failure`() = runTest {
        val uri = mockk<Uri>()
        val error = Exception("CSV error")
        coEvery { csvManager.importExpensesFromCsv(uri) } returns Result.Failure(error)

        viewModel.importExpenses(uri)
        advanceUntilIdle()

        val msg = viewModel.messageFlow.first()
        assertEquals("Ошибка импорта: CSV error", msg)
    }
}