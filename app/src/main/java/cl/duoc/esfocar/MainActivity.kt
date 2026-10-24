
package cl.duoc.esfocar

// Permiten iniciar la actividad y mostrar la interfaz con Compose.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// Componentes visuales utilizados por estudio y cuestionario.
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

// Herramientas para guardar el estado de la interfaz.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Herramientas de diseño y formato.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importamos la pantalla de inicio que creamos en otro archivo.
import cl.duoc.esfocar.screens.HomeScreen

// Tema que Android Studio generó para nuestro proyecto.
import cl.duoc.esfocar.ui.theme.EsfocarTheme

// Colores reutilizados en las pantallas que permanecen aquí.
private val VerdeForma = Color(0xFF17452F)
private val VerdeClaro = Color(0xFFE7F0E9)
private val FondoForma = Color(0xFFF6F7F3)

// ENUM: define las pantallas permitidas en esta primera versión.
// En vez de guardar cualquier texto, usamos opciones definidas.
private enum class Pantalla {
    Inicio,
    Estudio,
    Cuestionario
}

// ACTIVIDAD PRINCIPAL:
// Android llama a onCreate cuando se crea la actividad.
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la interfaz utilice el espacio detrás
        // de las barras del sistema cuando corresponda.
        enableEdgeToEdge()

        // setContent establece la interfaz con Jetpack Compose.
        setContent {
            // Aplica el tema del proyecto.
            EsfocarTheme {
                // Llama al componente que controla la navegación.
                FormaApp()
            }
        }
    }
}

// NAVEGACIÓN:
// Decide cuál de las tres pantallas se muestra.
@Composable
fun FormaApp() {

    // remember conserva el estado durante las recomposiciones.
    // mutableStateOf crea un estado observable por Compose.
    // Al cambiar pantalla, Compose actualiza la interfaz.
    var pantalla by remember {
        mutableStateOf(Pantalla.Inicio)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoForma
    ) {
        // WHEN funciona de forma similar a un switch:
        // ejecuta la sección correspondiente al valor de pantalla.
        when (pantalla) {

            // Mostramos HomeScreen, que está en otro archivo.
            Pantalla.Inicio -> HomeScreen(
                onEstudio = {
                    // Cambiamos el estado al pulsar "Ver módulos".
                    pantalla = Pantalla.Estudio
                },
                onCuestionario = {
                    // Cambiamos el estado al abrir la práctica.
                    pantalla = Pantalla.Cuestionario
                }
            )

            // Mostramos la pantalla de estudio.
            Pantalla.Estudio -> EstudioScreen(
                onVolver = {
                    // La acción vuelve a cambiar el estado.
                    pantalla = Pantalla.Inicio
                }
            )

            // Mostramos el cuestionario.
            Pantalla.Cuestionario -> CuestionarioScreen(
                onVolver = {
                    pantalla = Pantalla.Inicio
                }
            )
        }
    }
}

// PANTALLA DE ESTUDIO:
// Por ahora contiene información de ejemplo.
// Después la conectaremos con los contenidos del proyecto.
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
                            "y contenidos educativos ficticios."
                )
            }
        }

        // El botón ejecuta la acción que vuelve al inicio.
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

// PANTALLA DEL CUESTIONARIO:
// Gestiona la selección de una alternativa y muestra el resultado.
// En una próxima etapa trasladaremos esta lógica al ViewModel.
@Composable
fun CuestionarioScreen(onVolver: () -> Unit) {

    // -1 significa que no hay una respuesta seleccionada.
    var respuesta by remember {
        mutableStateOf(-1)
    }

    // Indica si ya se pulsó el botón de corrección.
    var corregido by remember {
        mutableStateOf(false)
    }

    // Lista de alternativas de nuestra pregunta demostrativa.
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

        // Recorremos la lista para generar una tarjeta por alternativa.
        // index es la posición de la alternativa y alternativa es su texto.
        alternativas.forEachIndexed { index, alternativa ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    // Destacamos visualmente la alternativa seleccionada.
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
                        // Indica qué alternativa está seleccionada.
                        selected = respuesta == index,

                        // Código ejecutado al seleccionar una alternativa.
                        onClick = {
                            respuesta = index
                            // Si cambia la respuesta, ocultamos el resultado anterior.
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

        // El botón se deshabilita mientras respuesta sea -1.
        Button(
            onClick = {
                corregido = true
            },
            enabled = respuesta != -1,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Comprobar respuesta")
        }

        // Solo mostramos la retroalimentación después de corregir.
        if (corregido) {

            // La segunda alternativa tiene índice 1 porque
            // las posiciones de las listas empiezan en cero.
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
