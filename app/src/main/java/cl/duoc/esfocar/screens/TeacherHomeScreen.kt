package cl.duoc.esfocar.screens

import android.net.Uri
import android.provider.OpenableColumns

// Animaciones de Compose.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

// Componentes visuales.
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Selector nativo de documentos de Android.
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// Colores institucionales de ESFOCAR.
private val Verde = Color(0xFF17452F)
private val Dorado = Color(0xFFC6A34A)
private val Fondo = Color(0xFFF6F7F3)

@Composable
fun TeacherHomeScreen(
    onModulos: () -> Unit = {},
    onCuestionarios: () -> Unit = {},
    onResultados: () -> Unit = {}
) {
    // Permite desplazarse por la pantalla si el contenido es largo.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado institucional.
        Text(
            text = "ESFOCAR",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Menú del profesor",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = Verde
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Acceso a la gestión de módulos.
        TarjetaProfesor(
            titulo = "Gestionar módulos",
            descripcion = "Crear y administrar módulos de estudio",
            alPulsar = onModulos
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Acceso a la creación de cuestionarios.
        TarjetaProfesor(
            titulo = "Crear cuestionarios",
            descripcion = "Agregar preguntas y alternativas",
            alPulsar = onCuestionarios
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Acceso a los resultados de los estudiantes.
        TarjetaProfesor(
            titulo = "Ver resultados",
            descripcion = "Revisar los puntajes de los estudiantes",
            alPulsar = onResultados
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Sección funcional para seleccionar material de estudio.
        SeccionArchivos()

        Spacer(modifier = Modifier.height(20.dp))

        // Ayuda desplegable para el profesor.
        SeccionAyuda()
    }
}

// Tarjeta reutilizable con animación de color.
@Composable
private fun TarjetaProfesor(
    titulo: String,
    descripcion: String,
    alPulsar: () -> Unit
) {
    // Recuerda si el usuario ha seleccionado esta tarjeta.
    var seleccionada by remember {
        mutableStateOf(false)
    }

    // Cambia gradualmente entre blanco y dorado.
    val colorTarjeta by animateColorAsState(
        targetValue = if (seleccionada) Dorado else Color.White,
        animationSpec = tween(durationMillis = 350),
        label = "Animación de color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Activa el efecto visual y ejecuta la navegación original.
                seleccionada = !seleccionada
                alPulsar()
            },
        colors = CardDefaults.cardColors(
            containerColor = colorTarjeta
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
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

// Sección que permite seleccionar un PDF o un documento Word.
@Composable
private fun SeccionArchivos() {
    val contexto = LocalContext.current

    // Guarda la referencia del archivo seleccionado.
    var archivoSeleccionado by remember {
        mutableStateOf<Uri?>(null)
    }

    // Abre el selector de documentos propio de Android.
    val selectorDocumentos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        // Solo actualiza el estado si se seleccionó un archivo.
        if (uri != null) {
            archivoSeleccionado = uri
        }
    }

    // Consulta el nombre visible del documento seleccionado.
    val nombreArchivo = archivoSeleccionado?.let { uri ->
        var nombre: String? = null

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

        nombre ?: uri.lastPathSegment ?: "Documento seleccionado"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
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
                text = "Selecciona documentos para tu material de apoyo.",
                color = Verde
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    // Filtra los tipos de documentos que puede elegir.
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

            if (nombreArchivo != null) {
                // Informa al profesor cuál fue el documento elegido.
                Text(
                    text = "Documento seleccionado",
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = nombreArchivo,
                    color = Verde
                )
            } else {
                Text(
                    text = "Ningún documento seleccionado",
                    color = Color.Gray
                )
            }
        }
    }
}

// Ayuda útil en lugar de los ejemplos de animación.
@Composable
private fun SeccionAyuda() {
    var mostrarAyuda by remember {
        mutableStateOf(false)
    }

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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Alterna entre mostrar y ocultar las instrucciones.
                    mostrarAyuda = !mostrarAyuda
                }
            ) {
                Text(
                    text = if (mostrarAyuda) {
                        "Ocultar instrucciones"
                    } else {
                        "Ver instrucciones"
                    }
                )
            }

            // Expande el contenido y lo oculta con una transición suave.
            AnimatedVisibility(
                visible = mostrarAyuda,
                enter = expandVertically(
                    animationSpec = tween(350),
                    expandFrom = Alignment.Top
                ) + fadeIn(animationSpec = tween(350)),
                exit = shrinkVertically(
                    animationSpec = tween(350),
                    shrinkTowards = Alignment.Top
                ) + fadeOut(animationSpec = tween(250))
            ) {
                Column(
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = "• Gestiona los módulos de estudio.\n" +
                                "• Crea cuestionarios para los estudiantes.\n" +
                                "• Consulta los resultados obtenidos.\n" +
                                "• Selecciona material de apoyo en PDF o Word.",
                        color = Verde
                    )
                }
            }
        }
    }
}
