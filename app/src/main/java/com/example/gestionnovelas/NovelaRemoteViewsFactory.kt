package com.example.gestionnovelas

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class NovelaRemoteViewsFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private val novels = mutableListOf<String>()

    override fun onCreate() {
        // Initialize the data set
        novels.add("Example Novela 1")
        novels.add("Example Novela 2")
    }

    override fun onDataSetChanged() {
        // Update the data set
    }

    override fun onDestroy() {
        novels.clear()
    }

    override fun getCount(): Int {
        return novels.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.novela_list_item)
        views.setTextViewText(R.id.novela_title, novels[position])
        return views
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}