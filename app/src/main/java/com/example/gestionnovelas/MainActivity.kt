package com.example.gestionnovelas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestionnovelas.ui.theme.GestionNovelasTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder


data class Novela(
    val titulo: String,
    val anioPublicacion: String,
    val notaMedia: String,
    val editorial: String,
    val temas: String,
    var esFavorita: Boolean = false,
    val reseñas: MutableList<String> = mutableListOf()
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionNovelasTheme {
                val navController = rememberNavController()
                val novelas = remember { mutableStateListOf<Novela>() }

                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") { WelcomeScreen(navController) }
                    composable("second") { SecondScreen(navController) }
                    composable("addNovela") { AddNovelaScreen(navController, novelas) }
                    composable("detallesNovelas") { DetallesNovelasScreen(navController, novelas) }

                    // Navegación a la pantalla de reseñas
                    composable("reseñas/{tituloNovela}") { backStackEntry ->
                        val tituloNovela = backStackEntry.arguments?.getString("tituloNovela")
                        val novela = novelas.find { it.titulo == tituloNovela }
                        novela?.let {
                            ResenasScreen(navController, it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val customFont = FontFamily(Font(R.font.anton))
    val backgroundImage = painterResource(id = R.drawable.fondo_libros)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido al programa de gestión de novelas",
                fontFamily = customFont,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("second") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Comencemos")
            }
        }
    }
}

@Composable
fun SecondScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("addNovela") },  //Navegar a la pantalla de agregar novelas
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Agregar Novelas")
        }
        Button(
            onClick = { navController.navigate("detallesNovelas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Ver Detalles de las Novelas")
        }
    }
}

@Composable
fun AddNovelaScreen(navController: NavHostController, novelas: MutableList<Novela>) {
    var titulo by remember { mutableStateOf("") }
    var anioPublicacion by remember { mutableStateOf("") }
    var notaMedia by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf("") }
    var temas by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = anioPublicacion,
            onValueChange = { anioPublicacion = it },
            label = { Text("Año de Publicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notaMedia,
            onValueChange = { notaMedia = it },
            label = { Text("Nota Media") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = editorial,
            onValueChange = { editorial = it },
            label = { Text("Editorial") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = temas,
            onValueChange = { temas = it },
            label = { Text("Temas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val nuevaNovela = Novela(titulo, anioPublicacion, notaMedia, editorial, temas)
                novelas.add(nuevaNovela) //Guardar la novela en la lista
                navController.popBackStack()  //Volver a la pantalla anterior
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar Novela")
        }
    }
}

@Composable
fun DetallesNovelasScreen(navController: NavHostController, novelas: MutableList<Novela>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(novelas) { novela ->
            NovelaItem(
                novela = novela,
                navController = navController,
                onEliminar = { novelas.remove(novela) },
                onToggleFavorita = {
                    novela.esFavorita = !novela.esFavorita
                }
            )
        }
    }
}

@Composable
fun ResenasScreen(navController: NavHostController, novela: Novela) {
    var nuevaReseña by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Reseñas de ${novela.titulo}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        //Mostrar las reseñas existentes
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(novela.reseñas) { reseña ->
                Text(text = reseña, modifier = Modifier.padding(8.dp))
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Campo para añadir una nueva reseña
        OutlinedTextField(
            value = nuevaReseña,
            onValueChange = { nuevaReseña = it },
            label = { Text("Escribe una nueva reseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Botón para añadir la reseña
        Button(
            onClick = {
                if (nuevaReseña.isNotEmpty()) {
                    novela.reseñas.add(nuevaReseña)  //Añadir la reseña a la lista
                    nuevaReseña = ""  //Limpiar los espacios del teclado
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir Reseña")
        }
    }
}

@Composable
fun NovelaItem(novela: Novela, navController: NavHostController, onEliminar: () -> Unit, onToggleFavorita: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = novela.titulo, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Año: ${novela.anioPublicacion}")
            Text(text = "Nota Media: ${novela.notaMedia}")
            Text(text = "Editorial: ${novela.editorial}")
            Text(text = "Temas: ${novela.temas}")
        }

        //Botón para marcar como favorita
        IconButton(onClick = { onToggleFavorita() }) {
            Icon(
                imageVector = if (novela.esFavorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null
            )
        }

        //Botón para eliminar
        IconButton(onClick = { onEliminar() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar"
            )
        }

        //Botón para ver las reseñas
        IconButton(onClick = { navController.navigate("reseñas/${novela.titulo}") }) {
            Icon(
                painter = painterResource(id = R.drawable.icono_resena),
                contentDescription = "Reseñas"
            )
        }
    }
}
