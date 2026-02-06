package uz.finlog.costaccounting.ui.widget

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WidgetManagerImpl(private val context: Context) : WidgetManager {
    override fun refreshWidget() {
        val request = OneTimeWorkRequestBuilder<UpdateWidgetWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}