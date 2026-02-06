package uz.finlog.costaccounting.ui

import android.app.Application
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import uz.finlog.costaccounting.di.appModule
import uz.finlog.costaccounting.di.databaseModule
import uz.finlog.costaccounting.di.repositoryModule
import uz.finlog.costaccounting.di.workerModule

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        startKoin {
            androidContext(this@MyApp)
            workManagerFactory()
            modules(appModule, databaseModule, repositoryModule, workerModule)
        }
    }
}