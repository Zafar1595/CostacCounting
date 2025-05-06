package uz.finlog.costaccounting.ui

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uz.finlog.costaccounting.di.appModule
import uz.finlog.costaccounting.di.databaseModule
import uz.finlog.costaccounting.di.repositoryModule

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModule, databaseModule, repositoryModule)
        }
    }
}