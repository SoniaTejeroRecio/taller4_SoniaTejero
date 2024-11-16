package com.example.gestionnovelas

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService

class NovelaWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NovelaRemoteViewsFactory(this.applicationContext, intent)
    }
}