package com.example.gestionnovelas

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi

class NovelaAppWidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.S)
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.novela_loading_appwidget)

            // Set the text for the widget
            views.setTextViewText(R.id.novela_title, "Example Novela Title")
            views.setTextViewText(R.id.novela_editorial, "Example Novela Editorial")
            views.setTextViewText(R.id.novela_details, "Example Novela Details")

            // Create an Intent to launch MainActivity when clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.novela_title, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.gestionnovelas.CHECKBOX_CHANGED") {
            val isChecked = intent.getBooleanExtra(RemoteViews.EXTRA_CHECKED, false)
            // Handle checkbox state change
        }
    }
}