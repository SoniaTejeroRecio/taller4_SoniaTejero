package com.example.gestionnovelas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestionnovelas.ui.theme.GestionNovelasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionNovelasTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") { WelcomeScreen(navController) }
                    composable("second") { SecondScreen(navController) }
                    composable("addNovela") { AddNovelaScreen(navController) }
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

/*@Composable
fun SecondScreen() {
    Text(text = "Esta es la segunda pantalla")
}

 */
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
            onClick = { navController.navigate("addNovela") },  // Navegar a la pantalla de agregar novelas
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Agregar Novelas")
        }
        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Eliminar Novelas")
        }
        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Ver Detalles de las Novelas")
        }
        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Marcar Novelas Favoritas")
        }
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Añadir Reseñas")
        }
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Agregar Reseñas")
        }
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Otras opciones")
        }
    }
}



    @Composable
    fun AddNovelaScreen(navController: NavHostController) {
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
            Text(text = "Introduce los datos de la novela", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el título de la novela
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para el año de publicación
            OutlinedTextField(
                value = anioPublicacion,
                onValueChange = { anioPublicacion = it },
                label = { Text("Año de Publicación") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para la nota media
            OutlinedTextField(
                value = notaMedia,
                onValueChange = { notaMedia = it },
                label = { Text("Nota Media") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para la editorial
            OutlinedTextField(
                value = editorial,
                onValueChange = { editorial = it },
                label = { Text("Editorial") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo para los temas
            OutlinedTextField(
                value = temas,
                onValueChange = { temas = it },
                label = { Text("Temas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar la novela
            Button(
                onClick = {
                    // Aquí puedes manejar el guardado de los datos
                    // Volver a la pantalla anterior tras guardar la novela
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Guardar Novela")
            }
        }
    }

