package uz.finlog.costaccounting.ui.screens.home.detail

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.entity.Expense
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

//    private lateinit var repository: ExpenseRepository
//    private lateinit var viewModel: DetailViewModel
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        repository = mockk(relaxed = true)
//        viewModel = DetailViewModel(repository)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        Dispatchers.shutdownTestDispatchers()
//    }
//
//    @Test
//    fun `loadExpense updates selectedExpense`() = runTest {
//        val expectedExpense = Expense(
//            id = 1,
//            title = "Lunch",
//            comment = "Business lunch",
//            amount = 12.5,
//            date = System.currentTimeMillis()
//        )
//        coEvery { repository.getExpenseById(1) } returns expectedExpense
//
//        viewModel.loadExpense(1)
//        advanceUntilIdle()
//
//        val result = viewModel.selectedExpense.value
//        assertNotNull(result)
//        assertEquals(expectedExpense, result)
//    }
//
//    @Test
//    fun `deleteExpense calls repository with correct expense`() = runTest {
//        val expense = Expense(1, "Taxi", "Trip to airport", 20.0, System.currentTimeMillis())
//        coEvery { repository.deleteExpense(expense) } just Runs
//
//        viewModel.deleteExpense(expense)
//        advanceUntilIdle()
//
//        coVerify(exactly = 1) { repository.deleteExpense(expense) }
//    }
//
//    @Test
//    fun `updateExpense calls repository with correct expense`() = runTest {
//        val expense = Expense(1, "Groceries", "Weekly shopping", 100.0, System.currentTimeMillis())
//        coEvery { repository.updateExpense(expense) } just Runs
//
//        viewModel.updateExpense(expense)
//        advanceUntilIdle()
//
//        coVerify(exactly = 1) { repository.updateExpense(expense) }
//    }
}