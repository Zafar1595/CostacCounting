package uz.finlog.costaccounting.ui.screens.home

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
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import java.util.Calendar
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        expenseRepository = mockk()
        categoryRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `filteredExpenses returns filtered list based on query`() = runTest(testDispatcher) {
        val mockExpenseList = listOf(
            Expense(
                id = 1,
                title = "Coffee",
                comment = "Comment one",
                amount = 4.99,
                date = Calendar.getInstance().timeInMillis,
                categoryId = 1
            ),
            Expense(
                id = 2,
                title = "Tea",
                comment = "Comment two",
                amount = 2.99,
                date = Calendar.getInstance().timeInMillis,
                categoryId = 2
            )
        )

        every { expenseRepository.getAllExpenses() } returns flowOf(mockExpenseList)
        every { categoryRepository.getAllCategories() } returns flowOf(emptyList())

        viewModel = HomeViewModel(
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository
        )

        val job = launch {
            viewModel.filteredExpenses.collect {}
        }

        viewModel.onSearchQueryChanged("Coffee")
        advanceUntilIdle()

        val result = viewModel.filteredExpenses.first()

        assertEquals(1, result.size)
        assertEquals("Coffee", result.first().title)

        job.cancel()
    }
}