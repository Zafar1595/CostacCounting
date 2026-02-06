package uz.finlog.costaccounting.ui.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class CostacWidgetReceiver : GlanceAppWidgetReceiver() {
    // Указываем системе, какой именно виджет рисовать
    override val glanceAppWidget: GlanceAppWidget = CostacWidget()
}