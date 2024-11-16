package com.example.gestionnovelas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private val novels = mutableListOf<Novel>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Configuración del tema antes de cargar la vista
        setTheme(R.style.Theme_GestionNovelas)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greetingTextView: TextView = findViewById(R.id.greetingTextView)
        val buttonMainScreen: Button = findViewById(R.id.buttonMainScreen)
        val buttonSensor: Button = findViewById(R.id.buttonSensor)
        val buttonWidgetConfig: Button = findViewById(R.id.buttonWidgetConfig)
        val addNovelButton: Button = findViewById(R.id.addNovelButton)

        greetingTextView.text = getGreetingMessage()

        buttonMainScreen.setOnClickListener {
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
        }

        buttonSensor.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }

        buttonWidgetConfig.setOnClickListener {
            val intent = Intent(this, NovelaSummaryActivity::class.java).apply {
                putExtra("NOVELA_TITLE", "Ejemplo de Novela")
                putExtra("NOVELA_EDITORIAL", "Editorial Ejemplo")
                putExtra("NOVELA_DETAILS", "Detalles de la novela de ejemplo")
            }
            startActivity(intent)
        }

        addNovelButton.setOnClickListener {
            showAddNovelDialog()
        }
    }

    private fun showAddNovelDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_novel, null)
        val novelNameEditText: EditText = dialogView.findViewById(R.id.novelNameEditText)
        val authorEditText: EditText = dialogView.findViewById(R.id.authorEditText)
        val publisherEditText: EditText = dialogView.findViewById(R.id.publisherEditText)
        val ratingEditText: EditText = dialogView.findViewById(R.id.ratingEditText)

        AlertDialog.Builder(this)
            .setTitle("Agregar Novela")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val novelName = novelNameEditText.text.toString()
                val author = authorEditText.text.toString()
                val publisher = publisherEditText.text.toString()
                val rating = ratingEditText.text.toString().toFloatOrNull() ?: 0f

                val novel = Novel(novelName, author, publisher, rating)
                novels.add(novel)
                saveNovelToFirestore(novel)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveNovelToFirestore(novel: Novel) {
        db.collection("novels")
            .add(novel)
            .addOnSuccessListener { documentReference ->
                println("Novela añadida con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error añadiendo novela: $e")
            }
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Buenos días"
            in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}

data class Novel(val name: String, val author: String, val publisher: String, val rating: Float)