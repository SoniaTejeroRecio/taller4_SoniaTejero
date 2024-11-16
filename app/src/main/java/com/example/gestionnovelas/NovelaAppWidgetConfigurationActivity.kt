package com.example.gestionnovelas

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.gestionnovelas.NovelaAppWidgetProvider.Companion.updateAppWidget

class NovelaAppWidgetConfigurationActivity : Activity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.novela_appwidget_configuration)

        // Set the result to CANCELED in case the user backs out
        setResult(RESULT_CANCELED)

        // Find the widget id from the intent
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an invalid appWidgetId, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val yellowButton = findViewById<Button>(R.id.yellow_button)
        yellowButton.setOnClickListener {
            val context = this@NovelaAppWidgetConfigurationActivity

            val titleEditText = findViewById<EditText>(R.id.novela_title_input)
            val editorialEditText = findViewById<EditText>(R.id.novela_editorial_input)
            val detailsEditText = findViewById<EditText>(R.id.novela_details_input)

            val novelaTitle = titleEditText.text.toString()
            val novelaEditorial = editorialEditText.text.toString()
            val novelaDetails = detailsEditText.text.toString()

            // Launch the summary activity
            val summaryIntent = Intent(context, NovelaSummaryActivity::class.java).apply {
                putExtra("NOVELA_TITLE", novelaTitle)
                putExtra("NOVELA_EDITORIAL", novelaEditorial)
                putExtra("NOVELA_DETAILS", novelaDetails)
            }
            startActivity(summaryIntent)
        }
    }
}