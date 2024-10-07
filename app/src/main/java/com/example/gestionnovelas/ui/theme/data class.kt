package com.example.gestionnovelas.ui.theme

data class Novela(
    val titulo: String,
    val anioPublicacion: String,
    val notaMedia: String,
    val editorial: String,
    val temas: String,
    var esFavorita: Boolean = false // Para marcar si es favorita o no
)
