package uz.finlog.costaccounting.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.finlog.costaccounting.data.dao.AppDatabase
import uz.finlog.costaccounting.data.dao.ExpenseDao
import uz.finlog.costaccounting.data.repository.ExpenseRepositoryImpl
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.ui.screens.home.HomeViewModel
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreenViewModel
import uz.finlog.costaccounting.ui.screens.stats.StatsScreenViewModel

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AddExpenseScreenViewModel(get()) }
    viewModel { StatsScreenViewModel(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "expense_database"
        ).build()
    }
    single<ExpenseDao> { get<AppDatabase>().expenseDao() }
}

val repositoryModule = module {
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
}