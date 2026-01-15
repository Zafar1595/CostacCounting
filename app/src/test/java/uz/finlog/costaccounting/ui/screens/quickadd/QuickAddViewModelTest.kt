package uz.finlog.costaccounting.ui.screens.quickadd

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Category
import uz.finlog.costaccounting.entity.Expense
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class QuickAddViewModelTest {

    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var viewModel: QuickAddViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        expenseRepository = mockk(relaxed = true)
        categoryRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `categories flow emits data from repository`() = runTest(testDispatcher) {
        val categories = listOf(
            Category(id = 1, name = "Food", image = "🍔"),
            Category(id = 2, name = "Transport", image = "🚌")
        )
        every { categoryRepository.getAllCategories() } returns flowOf(categories)

        viewModel = QuickAddViewModel(expenseRepository, categoryRepository)

        // Trigger init
        advanceUntilIdle()

        val result = viewModel.categories.first()
        assertEquals(2, result.size)
        assertEquals("Food", result[0].name)
    }

    @Test
    fun `saveExpense inserts expense and emits success`() = runTest(testDispatcher) {
        every { categoryRepository.getAllCategories() } returns flowOf(emptyList())
        viewModel = QuickAddViewModel(expenseRepository, categoryRepository)

        val amount = 100.0
        val categoryId = 1
        val comment = "Lunch"

        var saveSuccessEmitted = false
        val job = launch {
            viewModel.saveSuccess.collect {
                saveSuccessEmitted = true
            }
        }

        viewModel.saveExpense(amount, categoryId, comment)
        advanceUntilIdle()

        coVerify {
            expenseRepository.insert(match {
                it.amount == amount && it.categoryId == categoryId && it.comment == comment
            })
        }

        assert(saveSuccessEmitted)
        job.cancel()
    }
}