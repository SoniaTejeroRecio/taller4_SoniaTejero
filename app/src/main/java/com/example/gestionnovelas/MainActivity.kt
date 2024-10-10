package com.example.gestionnovelas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.tasks.await

data class Novela(
    val titulo: String = "",
    val anioPublicacion: String = "",
    val notaMedia: String = "",
    val editorial: String = "",
    val temas: String = "",
    var esFavorita: Boolean = false,
    val reseñas: MutableList<String> = mutableListOf()
) {
    // Constructor vacío necesario para Firestore
    constructor() : this("", "", "", "", "", false, mutableListOf())
}

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Si el mensaje contiene datos
        remoteMessage.data?.let {
            sendNotification(it["title"], it["body"])
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        // Crear un canal de notificación si es necesario (para Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channelName = "Default"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        // Verificar si el permiso de notificaciones está concedido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Si el permiso no está concedido, no enviar la notificación
                return
            }
        }

        // Crear la notificación
        val builder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.ic_notification) // Asegúrate de que `ic_notification` existe en `res/drawable`
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // Mostrar la notificación
            notify(0, builder.build())
        }
    }
}



class MainActivity : ComponentActivity() {
    //Inicializa Firestore a nivel de clase para que esté disponible en todos los métodos
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inicializa Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        setContent {
            GestionNovelasTheme {
                val navController = rememberNavController()
                val novelas = remember { mutableStateListOf<Novela>() }

                //Cargar las novelas desde Firebase cuando la app se inicie
                LaunchedEffect(Unit) {
                    cargarNovelasDesdeFirebase(novelas)
                }

                //Listener para cambios en tiempo real en la colección de novelas
                db.collection("novelas").addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Manejo de error
                        return@addSnapshotListener
                    }

                    //Limpiar la lista local antes de actualizarla con los datos de Firebase
                    novelas.clear()

                    //Añadir cada novela del snapshot a la lista
                    snapshot?.documents?.forEach { document ->
                        val novela = document.toObject(Novela::class.java)
                        novela?.let { novelas.add(it) }
                    }
                }

                // Navegación entre pantallas
                NavHost(navController = navController, startDestination = "welcome") {
                    composable("welcome") { WelcomeScreen(navController) }
                    composable("second") { SecondScreen(navController) }
                    composable("addNovela") { AddNovelaScreen(navController, novelas, db) }
                    composable("detallesNovelas") {
                        DetallesNovelasScreen(navController, novelas, db)  // Pasamos db a DetallesNovelasScreen
                    }
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

    //Función para cargar las novelas desde Firebase Firestore
    private suspend fun cargarNovelasDesdeFirebase(novelas: MutableList<Novela>) {
        try {
            val snapshot = db.collection("novelas").get().await()

            //Limpiar la lista antes de agregar las nuevas novelas
            novelas.clear()

            //Mapear cada documento a una instancia de Novela y añadirla a la lista
            snapshot.documents.forEach { document ->
                val novela = document.toObject(Novela::class.java)
                novela?.let { novelas.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace() // Manejo de error
        }
    }

    //Función para guardar una novela en Firebase Firestore
    fun guardarNovelaEnFirebase(novela: Novela) {
        db.collection("novelas")
            .add(novela)
            .addOnSuccessListener {
                //Acción si se guarda exitosamente
            }
            .addOnFailureListener {
                //Manejo de error
            }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val customFont = remember { FontFamily(Font(R.font.anton)) }  //Asegúrate de que la fuente existe
    val backgroundImage = painterResource(id = R.drawable.fondo_libros) // Asegúrate de que la imagen existe

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
            onClick = { navController.navigate("addNovela") }, // Navega a la pantalla para agregar novelas
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Agregar Novelas")
        }

        Button(
            onClick = { navController.navigate("detallesNovelas") }, // Navega a la pantalla de detalles
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Ver Detalles de las Novelas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        NavigationButtons(navController) //Usamos los botones de navegación comunes
    }
}

@Composable
fun AddNovelaScreen(navController: NavHostController, novelas: MutableList<Novela>, db: FirebaseFirestore) {
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
                novelas.add(nuevaNovela)
                db.collection("novelas").add(nuevaNovela)
                navController.navigate("detallesNovelas")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar Novela")
        }
    }
}

@Composable
fun DetallesNovelasScreen(navController: NavHostController, novelas: MutableList<Novela>, db: FirebaseFirestore) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(novelas) { novela ->
            NovelaItem(
                novela = novela,
                navController = navController,
                db = db, //Añadido para pasar Firestore
                onEliminar = { novelas.remove(novela) },
                onToggleFavorita = {
                    novela.esFavorita = !novela.esFavorita
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    NavigationButtons(navController)
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

        //Añadir nuevas reseñas
        OutlinedTextField(
            value = nuevaReseña,
            onValueChange = { nuevaReseña = it },
            label = { Text("Escribe una nueva reseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Botón para añadir reseña
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

        Spacer(modifier = Modifier.height(16.dp))
        NavigationButtons(navController)
    }
}

@Composable
fun NovelaItem(novela: Novela, navController: NavHostController, db: FirebaseFirestore, onEliminar: () -> Unit, onToggleFavorita: () -> Unit) {
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

        //Botón para eliminar la novela
        IconButton(onClick = {
            //Elimina la novela de Firestore
            db.collection("novelas").whereEqualTo("titulo", novela.titulo)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //Elimina el documento correspondiente en Firestore
                        db.collection("novelas").document(document.id).delete()
                            .addOnSuccessListener {
                                //Después de la eliminación exitosa, elimina la novela de la lista local
                                onEliminar()
                            }
                            .addOnFailureListener { e ->
                                //Manejo del error en la eliminación de Firestore
                                e.printStackTrace()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    //Manejo de error al buscar el documento
                    e.printStackTrace()
                }
        }) {
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

@Composable
fun NavigationButtons(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //Botón para volver atrás
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }

        //Botón para ir a la pantalla principal (Home)
        Button(onClick = { navController.navigate("welcome") }) {
            Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Home")
        }
    }
}
