package cl.duoc.esfocar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.esfocar.screens.HomeScreen
import cl.duoc.esfocar.ui.theme.EsfocarTheme
import kotlinx.coroutines.launch

// Colores compartidos de FORMA+.
private val VerdeForma = Color(0xFF17452F)
private val VerdeClaro = Color(0xFFE7F0E9)
private val DoradoForma = Color(0xFFC6A34A)
private val FondoForma = Color(0xFFF6F7F3)

// Identifica las pantallas disponibles.
private enum class Pantalla {
    Inicio,
    Estudio,
    Cuestionario
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EsfocarTheme {
                FormaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormaApp() {
    var pantalla by remember {
        mutableStateOf(Pantalla.Inicio)
    }

    // Estado del menú lateral y tareas para abrirlo o cerrarlo.
    val estadoMenu = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "FORMA+",
                        color = DoradoForma,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Tu aprendizaje, a tu ritmo",
                        color = VerdeForma
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cada opción cambia la pantalla y cierra el menú.
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = pantalla == Pantalla.Inicio,
                        onClick = {
                            pantalla = Pantalla.Inicio
                            alcance.launch { estadoMenu.close() }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    NavigationDrawerItem(
                        label = { Text("Módulos de estudio") },
                        selected = pantalla == Pantalla.Estudio,
                        onClick = {
                            pantalla = Pantalla.Estudio
                            alcance.launch { estadoMenu.close() }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    NavigationDrawerItem(
                        label = { Text("Cuestionarios") },
                        selected = pantalla == Pantalla.Cuestionario,
                        onClick = {
                            pantalla = Pantalla.Cuestionario
                            alcance.launch { estadoMenu.close() }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Scaffold(
            containerColor = FondoForma,
            topBar = {
                // Barra superior con botón para abrir el menú.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                            color = VerdeForma
                        )
                    }

                    Text(
                        text = "FORMA+",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeForma
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
                    // Usa la pantalla HomeScreen que está en otro archivo.
                    Pantalla.Inicio -> HomeScreen(
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
            color = VerdeForma
        )

        Text(
            text = "Selecciona un contenido para comenzar a repasar."
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = VerdeClaro
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Módulo 1: Repaso de contenidos",
                    fontWeight = FontWeight.Bold,
                    color = VerdeForma
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aquí incorporaremos material de estudio " +
                            "y contenidos educativos."
                )
            }
        }

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio")
        }
    }
}

@Composable
fun CuestionarioScreen(onVolver: () -> Unit) {
    var respuesta by remember {
        mutableStateOf(-1)
    }
    var corregido by remember {
        mutableStateOf(false)
    }

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
            color = VerdeForma
        )

        Text(text = "Pregunta 1 de práctica")

        Text(
            text = "¿Qué estrategia ayuda a reforzar el aprendizaje?",
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        alternativas.forEachIndexed { index, alternativa ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (respuesta == index) {
                        VerdeClaro
                    } else {
                        Color.White
                    }
                ),
                shape = RoundedCornerShape(12.dp)
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
                            respuesta = index
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

        Button(
            onClick = { corregido = true },
            enabled = respuesta != -1,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Comprobar respuesta")
        }

        if (corregido) {
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
                        VerdeForma
                    } else {
                        Color(0xFF9B2424)
                    }
                )
            }
        }

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio")
        }
    }
}
