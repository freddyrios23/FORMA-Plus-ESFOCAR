package cl.duoc.esfocar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.esfocar.ui.theme.EsfocarTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

// ----------------------------------------------------
// 1. COLORES DE LA APLICACIÓN
// Centralizamos los colores para reutilizarlos y mantener
// una identidad visual consistente en todas las pantallas.
// Son colores propuestos para el diseño, no códigos oficiales.
// ----------------------------------------------------
private val VerdeInstitucional = Color(0xFF17452F)
private val VerdeClaro = Color(0xFFE7F0E9)
private val Dorado = Color(0xFFC6A34A)
private val Fondo = Color(0xFFF6F7F3)

// ----------------------------------------------------
// 2. PANTALLAS DISPONIBLES
// El enum permite identificar las pantallas sin usar
// textos sueltos como "inicio" o "estudio" en todo el código.
// ----------------------------------------------------
private enum class Pantalla {
    Inicio,
    Estudio,
    Cuestionario
}

// ----------------------------------------------------
// 3. ACTIVIDAD PRINCIPAL DE ANDROID
// Es el punto de entrada de la interfaz de la aplicación.
// ----------------------------------------------------
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la interfaz pueda dibujarse detrás
        // de las barras del sistema cuando corresponda.
        enableEdgeToEdge()

        // setContent permite crear la interfaz con Compose.
        // EsfocarTheme aplica el tema configurado en el proyecto.
        setContent {
            EsfocarTheme {
                FormaApp()
            }
        }
    }
}

// ----------------------------------------------------
// 4. COMPONENTE PRINCIPAL Y NAVEGACIÓN
// Aquí guardamos cuál pantalla debe mostrarse.
// remember conserva el estado durante las recomposiciones.
// mutableStateOf avisa a Compose cuando cambia el valor.
// ----------------------------------------------------

@Composable
fun FormaApp() {

    var pantalla by remember {
        mutableStateOf(Pantalla.Inicio)
    }

    // Controla si el menú está abierto o cerrado.
    val estadoMenu = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    // Permite abrir y cerrar el menú con animación.
    val alcance = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = estadoMenu,

        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "FORMA+",
                        color = Dorado,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Tu aprendizaje, a tu ritmo",
                        color = VerdeInstitucional
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Divider()

                    // Opción Inicio
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = pantalla == Pantalla.Inicio,
                        onClick = {
                            pantalla = Pantalla.Inicio
                            alcance.launch {
                                estadoMenu.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Opción Módulos
                    NavigationDrawerItem(
                        label = {
                            Text("Módulos de estudio")
                        },
                        selected = pantalla == Pantalla.Estudio,
                        onClick = {
                            pantalla = Pantalla.Estudio
                            alcance.launch {
                                estadoMenu.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Opción Cuestionarios
                    NavigationDrawerItem(
                        label = { Text("Cuestionarios") },
                        selected =
                            pantalla == Pantalla.Cuestionario,
                        onClick = {
                            pantalla = Pantalla.Cuestionario
                            alcance.launch {
                                estadoMenu.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {

        Scaffold(
            containerColor = Fondo,

            // Barra superior con el botón de menú.
            topBar = {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            alcance.launch {
                                if (estadoMenu.isClosed) {
                                    estadoMenu.open()
                                } else {
                                    estadoMenu.close()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "☰",
                            fontSize = 28.sp,
                            color = VerdeInstitucional
                        )
                    }

                    Text(
                        text = "FORMA+",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeInstitucional
                    )
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (pantalla) {

                    Pantalla.Inicio -> InicioScreen(
                        onEstudio = {
                            pantalla = Pantalla.Estudio
                        },
                        onCuestionario = {
                            pantalla = Pantalla.Cuestionario
                        }
                    )

                    Pantalla.Estudio -> EstudioScreen(
                        onVolver = {
                            pantalla = Pantalla.Inicio
                        }
                    )

                    Pantalla.Cuestionario -> CuestionarioScreen(
                        onVolver = {
                            pantalla = Pantalla.Inicio
                        }
                    )
                }
            }
        }
    }
}


// ----------------------------------------------------
// 5. PANTALLA DE INICIO
// Presenta el nombre de la aplicación y accesos principales.
// Los parámetros reciben acciones para navegar a otras pantallas.
// ----------------------------------------------------
@Composable
fun InicioScreen(
    onEstudio: () -> Unit,
    onCuestionario: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()

            // Permite desplazarse si el contenido supera
            // el espacio vertical disponible.
            .verticalScroll(rememberScrollState())
            .padding(20.dp),

        // Separa verticalmente los elementos de la pantalla.
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Tarjeta superior con el nombre y bienvenida.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = VerdeInstitucional
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "FORMA+",
                    color = Dorado,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tu aprendizaje, a tu ritmo",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Estudia, practica y comprueba tus conocimientos.",
                    color = Color.White
                )
            }
        }

        // Títulos que orientan al usuario.
        Text(
            text = "¡Bienvenido/a!",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeInstitucional
        )

        Text(
            text = "¿Qué quieres hacer hoy?",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        // Reutilizamos la tarjeta para entrar a cada sección.
        OpcionCard(
            titulo = "Módulos de estudio",
            descripcion = "Repasa contenidos y conceptos clave.",
            boton = "Ver módulos",
            onClick = onEstudio
        )

        OpcionCard(
            titulo = "Cuestionarios",
            descripcion = "Responde preguntas y recibe retroalimentación.",
            boton = "Comenzar práctica",
            onClick = onCuestionario
        )

        Text(
            text = "Aprender · Practicar · Mejorar",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = VerdeInstitucional,
            fontWeight = FontWeight.Medium
        )
    }
}

// ----------------------------------------------------
// 6. TARJETA REUTILIZABLE
// En vez de repetir todo el diseño, recibimos el título,
// descripción, texto del botón y acción como parámetros.
// ----------------------------------------------------
@Composable
fun OpcionCard(
    titulo: String,
    descripcion: String,
    boton: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeInstitucional
            )

            Text(
                text = descripcion,
                color = Color.DarkGray
            )

            // onClick ejecuta la acción recibida al pulsar.
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeInstitucional
                )
            ) {
                Text(boton)
            }
        }
    }
}

// ----------------------------------------------------
// 7. PANTALLA DE ESTUDIO
// Por ahora es una estructura inicial de ejemplo.
// Luego conectaremos los módulos con datos reales del proyecto.
// ----------------------------------------------------
@Composable
fun EstudioScreen(onVolver: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Módulos de estudio",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeInstitucional
        )

        Text(
            text = "Selecciona un contenido para comenzar a repasar."
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = VerdeClaro
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                Text(
                    text = "Módulo 1: Repaso de contenidos",
                    fontWeight = FontWeight.Bold,
                    color = VerdeInstitucional
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aquí incorporaremos material de estudio " +
                            "y contenidos educativos ficticios."
                )
            }
        }

        // Al pulsarlo, ejecuta la acción de volver al inicio.
        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeInstitucional
            )
        ) {
            Text("Volver al inicio")
        }
    }
}

// ----------------------------------------------------
// 8. PANTALLA DEL CUESTIONARIO
// respuesta guarda el índice de la alternativa seleccionada.
// corregido indica si el usuario pulsó el botón de corrección.
// Ambos estados hacen que Compose actualice la interfaz.
// ----------------------------------------------------
@Composable
fun CuestionarioScreen(onVolver: () -> Unit) {

    // -1 significa que todavía no se ha elegido una alternativa.
    var respuesta by remember {
        mutableStateOf(-1)
    }

    // Inicialmente no mostramos la corrección.
    var corregido by remember {
        mutableStateOf(false)
    }

    // Lista de alternativas de la pregunta demostrativa.
    val alternativas = listOf(
        "Memorizar sin comprender.",
        "Estudiar, practicar y revisar los errores.",
        "Responder sin leer las preguntas."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Cuestionario de práctica",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeInstitucional
        )

        Text(text = "Pregunta 1 de práctica")

        Text(
            text = "¿Qué estrategia ayuda a reforzar el aprendizaje?",
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Recorremos la lista para crear un botón de selección
        // por cada alternativa, evitando repetir código.
        alternativas.forEachIndexed { index, alternativa ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (respuesta == index) {
                        VerdeClaro
                    } else {
                        Color.White
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected = respuesta == index,
                        onClick = {
                            // Guardamos la nueva selección.
                            respuesta = index

                            // Si cambia la respuesta, ocultamos
                            // el resultado anterior.
                            corregido = false
                        }
                    )

                    Text(
                        text = alternativa,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        // No permite corregir si aún no hay respuesta seleccionada.
        Button(
            onClick = {
                corregido = true
            },
            enabled = respuesta != -1,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeInstitucional
            )
        ) {
            Text("Comprobar respuesta")
        }

        // Solo mostramos la retroalimentación después de corregir.
        if (corregido) {

            // La alternativa con índice 1 es la correcta.
            val correcta = respuesta == 1

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (correcta) {
                        VerdeClaro
                    } else {
                        Color(0xFFFFE8E6)
                    }
                )
            ) {
                Text(
                    text = if (correcta) {
                        "¡Correcto! Practicar y revisar los errores " +
                                "ayuda a reforzar el aprendizaje."
                    } else {
                        "Respuesta incorrecta. Revisa las alternativas " +
                                "e inténtalo nuevamente."
                    },
                    modifier = Modifier.padding(16.dp),
                    color = if (correcta) {
                        VerdeInstitucional
                    } else {
                        Color(0xFF9B2424)
                    }
                )
            }
        }

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeInstitucional
            )
        ) {
            Text("Volver al inicio")
        }
    }
}
