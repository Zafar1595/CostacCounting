package uz.finlog.costaccounting.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.finlog.costaccounting.data.UserPreferences
import uz.finlog.costaccounting.data.dao.AppDatabase
import uz.finlog.costaccounting.data.dao.CategoryDao
import uz.finlog.costaccounting.data.dao.ExpenseDao
import uz.finlog.costaccounting.data.repository.CategoryRepositoryImpl
import uz.finlog.costaccounting.data.repository.ExpenseRepositoryImpl
import uz.finlog.costaccounting.domain.CategoryRepository
import uz.finlog.costaccounting.domain.ExpenseRepository
import uz.finlog.costaccounting.ui.screens.home.HomeViewModel
import uz.finlog.costaccounting.ui.screens.home.add_expense_screen.AddExpenseScreenViewModel
import uz.finlog.costaccounting.ui.screens.home.detail.DetailViewModel
import uz.finlog.costaccounting.ui.screens.settings.SettingsViewModel
import uz.finlog.costaccounting.ui.screens.stats.StatsScreenViewModel
import uz.finlog.costaccounting.util.CsvManager

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AddExpenseScreenViewModel(get(), get()) }
    viewModel { StatsScreenViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
    viewModel { DetailViewModel(get(), get()) }

    single { CsvManager(get()) }
}

val databaseModule = module {
    single {
        AppDatabase.create(get(), get())
    }
    single<ExpenseDao> { get<AppDatabase>().expenseDao() }

    single<CategoryDao> {get<AppDatabase>().categoryDao()}
}

val repositoryModule = module {
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }

    single { UserPreferences(androidContext()) }

    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}