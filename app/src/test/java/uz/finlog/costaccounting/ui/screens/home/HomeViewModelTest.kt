package uz.finlog.costaccounting.ui.screens.home

import android.widget.CalendarView
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import java.time.LocalDate
import java.util.Calendar
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: ExpenseRepository
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllexpenses updates expenses state`() = runTest(testDispatcher) {
        val mockList = listOf(
            Expense(1, "Salary", comment = "Comment one", 1000.0, System.currentTimeMillis()),
            Expense(2, "Dividends", comment = "Comment two", 500.0, System.currentTimeMillis())
        )

        every { repository.getAllExpenses() } returns flowOf(mockList)

        viewModel.getAllexpenses()
        advanceUntilIdle()

        assertEquals(2, viewModel.expenses.value.size)
        assertEquals("Salary", viewModel.expenses.value[0].title)
    }

    @Test
    fun `filteredExpenses returns filtered list based on query`() = runTest {
        val mockExpense = listOf(
            Expense(
                id = 1,
                title = "Coffee",
                comment = "Comment one",
                amount = 4.99,
                date = Calendar.getInstance().timeInMillis
            ),
            Expense(
                id = 2,
                title = "Tea",
                comment = "Comment two",
                amount = 2.99,
                date = Calendar.getInstance().timeInMillis
            )
        )

        every { repository.getAllExpenses() } returns flowOf(mockExpense)

        val viewModel = HomeViewModel(repository)

        val job = launch {
            viewModel.filteredExpenses.collect{} // Важно: активирует combine
        }

        viewModel.getAllexpenses()
        advanceUntilIdle()

        viewModel.onSearchQueryChanged("Coffee")
        advanceUntilIdle()

        val result = viewModel.filteredExpenses.first()

        assertEquals(1, result.size)
        assertEquals("Coffee", result.first().title)

        job.cancel() // Не забудь закрыть
    }
}