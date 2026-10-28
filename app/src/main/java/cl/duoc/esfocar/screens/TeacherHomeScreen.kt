
package cl.duoc.esfocar.screens

// Importaciones para las animaciones.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

// Importaciones para el diseño.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importaciones para seleccionar archivos del teléfono.
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

// Colores principales de ESFOCAR.
private val Verde = Color(0xFF17452F)
private val Dorado = Color(0xFFC6A34A)
private val Fondo = Color(0xFFF6F7F3)

@Composable
fun TeacherHomeScreen(
    onModulos: () -> Unit = {},
    onCuestionarios: () -> Unit = {},
    onResultados: () -> Unit = {}
) {
    // Permite desplazarse por la pantalla cuando el contenido no cabe.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de la pantalla del profesor.
        Text(
            text = "ESFOCAR",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Menú del profesor",
            fontSize = 22.sp,
            color = Verde
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Tarjeta para administrar los módulos.
        TarjetaProfesor(
            titulo = "Gestionar módulos",
            descripcion = "Crear y administrar módulos de estudio",
            alPulsar = onModulos
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Tarjeta para crear cuestionarios.
        TarjetaProfesor(
            titulo = "Crear cuestionarios",
            descripcion = "Agregar preguntas y alternativas",
            alPulsar = onCuestionarios
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Tarjeta para consultar los resultados.
        TarjetaProfesor(
            titulo = "Ver resultados",
            descripcion = "Revisar los puntajes de los estudiantes",
            alPulsar = onResultados
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Nueva sección para seleccionar material de estudio.
        SeccionArchivos()

        Spacer(modifier = Modifier.height(30.dp))

        // Ejemplo de animación de desplazamiento.
        EjemploDesplazamiento()

        Spacer(modifier = Modifier.height(25.dp))

        // Ejemplos de expansión, contracción y desvanecimiento.
        EjemploExpansion()

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// Tarjeta reutilizable que cambia suavemente de color.
@Composable
private fun TarjetaProfesor(
    titulo: String,
    descripcion: String,
    alPulsar: () -> Unit
) {
    // Guarda si la tarjeta está seleccionada.
    var seleccionada by remember { mutableStateOf(false) }

    // Anima el cambio entre blanco y dorado.
    val colorTarjeta by animateColorAsState(
        targetValue = if (seleccionada) Dorado else Color.White,
        animationSpec = tween(durationMillis = 600),
        label = "Color de la tarjeta"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Cambia el color y ejecuta la acción de la tarjeta.
                seleccionada = !seleccionada
                alPulsar()
            },
        colors = CardDefaults.cardColors(
            containerColor = colorTarjeta
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Verde
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = descripcion,
                color = Verde
            )
        }
    }
}

// Sección para seleccionar archivos PDF y Word.
@Composable
private fun SeccionArchivos() {
    // Contexto necesario para consultar el nombre del archivo.
    val contexto = LocalContext.current

    // Guarda la dirección del documento seleccionado.
    var archivoSeleccionado by remember {
        mutableStateOf<Uri?>(null)
    }

    // Abre el selector de documentos nativo del teléfono.
    // El usuario puede elegir PDF o documentos Word.
    val selectorDocumentos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        // Si el usuario selecciona un documento, guardamos su Uri.
        // Si cancela, no modificamos el archivo anterior.
        if (uri != null) {
            archivoSeleccionado = uri
        }
    }

    // Obtiene el nombre visible del archivo seleccionado.
    val nombreArchivo = archivoSeleccionado?.let { uri ->
        var nombre: String? = null

        // Consulta los metadatos del documento.
        contexto.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columna = cursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
                )

                if (columna >= 0) {
                    nombre = cursor.getString(columna)
                }
            }
        }

        // Si no se pudo obtener el nombre, muestra el último segmento.
        nombre ?: uri.lastPathSegment ?: "Documento seleccionado"
    }

    // Tarjeta visual de la sección de archivos.
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Material de estudio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Verde
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona un documento PDF o Word.",
                color = Verde
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Al pulsar, se abre el selector de archivos del sistema.
            Button(
                onClick = {
                    selectorDocumentos.launch(
                        arrayOf(
                            "application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        )
                    )
                }
            ) {
                Text(text = "Seleccionar archivo")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Muestra el nombre si ya se seleccionó un documento.
            if (nombreArchivo != null) {
                Text(
                    text = "Archivo seleccionado:",
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = nombreArchivo,
                    color = Verde
                )
            } else {
                Text(
                    text = "Todavía no has seleccionado un archivo.",
                    color = Color.Gray
                )
            }
        }
    }
}

// Ejemplo de desplazamiento usando animateIntOffsetAsState.
@Composable
private fun EjemploDesplazamiento() {
    var movido by remember { mutableStateOf(false) }

    // Convierte 80 dp a píxeles porque IntOffset trabaja con píxeles.
    val distancia = with(LocalDensity.current) {
        80.dp.roundToPx()
    }

    // Calcula la posición animada del cuadro.
    val desplazamiento by animateIntOffsetAsState(
        targetValue = if (movido) {
            IntOffset(x = distancia, y = 0)
        } else {
            IntOffset.Zero
        },
        animationSpec = tween(durationMillis = 600),
        label = "Desplazamiento del cuadro"
    )

    Text(
        text = "Ejemplo de desplazamiento",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Verde
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {
            // Alterna entre la posición inicial y la desplazada.
            movido = !movido
        }
    ) {
        Text(
            text = if (movido) "Volver al inicio" else "Mover cuadro"
        )
    }

    // Área donde aparece el cuadro animado.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset { desplazamiento }
                .size(60.dp)
                .background(Verde)
        )
    }
}

// Ejemplo de expansión y contracción vertical.
@Composable
private fun EjemploExpansion() {
    var expandida by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ayuda para el profesor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Verde
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    // Cambia el estado para mostrar u ocultar la ayuda.
                    expandida = !expandida
                }
            ) {
                Text(
                    text = if (expandida) "Ver menos" else "Ver más"
                )
            }

            // El contenido se expande y contrae verticalmente.
            AnimatedVisibility(
                visible = expandida,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 600),
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 600),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Text(
                    text = "Desde esta pantalla puedes gestionar los " +
                            "módulos de estudio, crear cuestionarios y " +
                            "consultar los resultados de los estudiantes.",
                    color = Verde,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Ejemplo de mostrar y ocultar mediante desvanecimiento.
    EjemploMostrarOcultar()
}

// Ejemplo de AnimatedVisibility con fadeIn y fadeOut.
@Composable
private fun EjemploMostrarOcultar() {
    var mostrar by remember { mutableStateOf(false) }

    Text(
        text = "Ejemplo de mostrar y ocultar",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Verde
    )

    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {
            // Alterna la visibilidad del mensaje.
            mostrar = !mostrar
        }
    ) {
        Text(
            text = if (mostrar) "Ocultar ayuda" else "Mostrar ayuda"
        )
    }

    // El mensaje aparece y desaparece suavemente.
    AnimatedVisibility(
        visible = mostrar,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Text(
            text = "Recuerda revisar los módulos y cuestionarios " +
                    "antes de finalizar tu trabajo.",
            color = Verde,
            modifier = Modifier.padding(12.dp)
        )
    }
}
