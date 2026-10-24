
package cl.duoc.esfocar.screens

// IMPORTS: herramientas que necesitamos para construir la interfaz.
// Compose es el sistema de Android que utilizamos para dibujarla.
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// COLORES: los guardamos en variables para reutilizarlos.
// Así, si queremos cambiar el verde, lo hacemos en un solo lugar.
private val VerdeForma = Color(0xFF17452F)
private val DoradoForma = Color(0xFFC6A34A)
private val FondoForma = Color(0xFFF6F7F3)

// @Composable indica que esta función construye una parte de la UI.
// Los parámetros son acciones que recibimos desde MainActivity:
// onEstudio abre los módulos y onCuestionario abre las preguntas.
@Composable
fun HomeScreen(
    onEstudio: () -> Unit,
    onCuestionario: () -> Unit
) {
    // Surface es el contenedor principal de la pantalla.
    Surface(
        modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible.
        color = FondoForma                 // Aplica el color de fondo.
    ) {
        // Column ordena los componentes verticalmente, uno debajo de otro.
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Permite desplazarse si el contenido no cabe en el celular.
                .verticalScroll(rememberScrollState())
                // Deja 20 dp de espacio entre el borde y el contenido.
                .padding(20.dp),

            // Separa los elementos de la columna por 20 dp.
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // CARD: tarjeta de bienvenida con fondo verde.
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp), // Redondea las esquinas.
                colors = CardDefaults.cardColors(
                    containerColor = VerdeForma
                )
            ) {
                // Otra columna organiza los textos dentro de la tarjeta.
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Nombre de nuestra aplicación.
                    Text(
                        text = "FORMA+",
                        color = DoradoForma,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Agrega espacio vertical entre los textos.
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tu aprendizaje, a tu ritmo",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Estudia, practica y comprueba tus conocimientos.",
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }
            }

            // Título que recibe al usuario.
            Text(
                text = "¡Bienvenido/a!",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeForma
            )

            // Pregunta que orienta al usuario.
            Text(
                text = "¿Qué quieres hacer hoy?",
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            // Creamos una tarjeta para entrar a los módulos de estudio.
            OpcionCard(
                titulo = "Módulos de estudio",
                descripcion = "Repasa contenidos y conceptos clave.",
                textoBoton = "Ver módulos",

                // Entregamos la acción que recibimos de MainActivity.
                onClick = onEstudio
            )

            // Creamos otra tarjeta usando exactamente el mismo componente.
            OpcionCard(
                titulo = "Cuestionarios",
                descripcion = "Responde preguntas y recibe retroalimentación.",
                textoBoton = "Comenzar práctica",
                onClick = onCuestionario
            )

            Text(
                text = "Aprender · Practicar · Mejorar",
                color = VerdeForma,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// COMPONENTE REUTILIZABLE:
// Evitamos escribir dos veces todo el diseño de una tarjeta.
// Los parámetros permiten cambiar el título, la descripción,
// el texto del botón y la acción que se ejecutará al pulsarlo.
@Composable
private fun OpcionCard(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // Sombra suave de la tarjeta.
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // El contenido de estos textos llega por parámetros.
            Text(
                text = titulo,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeForma
            )

            Text(
                text = descripcion,
                fontSize = 17.sp,
                color = Color.DarkGray
            )

            // Botón de Material 3.
            Button(
                onClick = onClick, // Ejecuta la acción recibida.
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),

                // Definimos ambos colores para que el texto sea legible.
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeForma,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = textoBoton,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
