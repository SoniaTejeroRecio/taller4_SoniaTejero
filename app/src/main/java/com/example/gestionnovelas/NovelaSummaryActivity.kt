package com.example.gestionnovelas

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class NovelaSummaryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novela_summary)

        val novelaTitle = intent.getStringExtra("NOVELA_TITLE")
        val novelaEditorial = intent.getStringExtra("NOVELA_EDITORIAL")
        val novelaDetails = intent.getStringExtra("NOVELA_DETAILS")

        val titleTextView = findViewById<TextView>(R.id.novela_title)
        val editorialTextView = findViewById<TextView>(R.id.novela_editorial)
        val detailsTextView = findViewById<TextView>(R.id.novela_details)

        titleTextView.text = "Nombre de la novela: $novelaTitle"
        editorialTextView.text = "Editorial: $novelaEditorial"
        detailsTextView.text = "Detalles: $novelaDetails"
    }
}