package cl.duoc.esfocar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import cl.duoc.esfocar.data.local.DatabaseProvider
import cl.duoc.esfocar.data.model.Cuestionario
import cl.duoc.esfocar.data.model.Question

// Colores de ESFOCAR
private val Verde = Color(0xFF17452F)
private val Fondo = Color(0xFFF6F7F3)

@Composable
fun CrearCuestionarioScreen(
    onVolver: () -> Unit = {}
) {

    // Guarda el título del cuestionario
    var titulo by remember {
        mutableStateOf("")
    }

    // Guarda el texto de la pregunta
    var pregunta by remember {
        mutableStateOf("")
    }

    // Guarda las alternativas
    var alternativa1 by remember {
        mutableStateOf("")
    }

    var alternativa2 by remember {
        mutableStateOf("")
    }

    var alternativa3 by remember {
        mutableStateOf("")
    }

    // Guarda el número de la alternativa correcta
    var respuestaCorrecta by remember {
        mutableStateOf(0)
    }

    // Guarda temporalmente las preguntas agregadas
    val preguntas = remember {
        mutableStateListOf<Question>()
    }

    // Obtiene el contexto de la aplicación
    val contexto = LocalContext.current

    // Obtiene el DAO para guardar los cuestionarios
    val cuestionarioDAO = remember {
        DatabaseProvider.obtenerDatabase(contexto).cuestionarioDAO()
    }

    // Permite ejecutar las operaciones de base de datos
    val scope = rememberCoroutineScope()

    // Mensaje para informar el resultado de una acción
    var mensaje by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Crear cuestionario",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título del cuestionario
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = {
                Text("Título del cuestionario")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nueva pregunta",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        // Texto de la pregunta
        OutlinedTextField(
            value = pregunta,
            onValueChange = { pregunta = it },
            label = {
                Text("Escribe la pregunta")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Alternativa 1
        OutlinedTextField(
            value = alternativa1,
            onValueChange = { alternativa1 = it },
            label = {
                Text("Alternativa 1")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Alternativa 2
        OutlinedTextField(
            value = alternativa2,
            onValueChange = { alternativa2 = it },
            label = {
                Text("Alternativa 2")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Alternativa 3
        OutlinedTextField(
            value = alternativa3,
            onValueChange = { alternativa3 = it },
            label = {
                Text("Alternativa 3")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selecciona la respuesta correcta:",
            fontWeight = FontWeight.Bold
        )

        // Selección de la alternativa correcta
        listOf(
            alternativa1,
            alternativa2,
            alternativa3
        ).forEachIndexed { indice, alternativa ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = respuestaCorrecta == indice + 1,
                    onClick = {
                        respuestaCorrecta = indice + 1
                    }
                )

                Text(
                    text = if (alternativa.isBlank()) {
                        "Alternativa ${indice + 1}"
                    } else {
                        alternativa
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Agrega la pregunta a la lista temporal
        Button(
            onClick = {

                if (
                    titulo.isNotBlank() &&
                    pregunta.isNotBlank() &&
                    alternativa1.isNotBlank() &&
                    alternativa2.isNotBlank() &&
                    alternativa3.isNotBlank() &&
                    respuestaCorrecta != 0
                ) {

                    // Crea la pregunta temporal
                    val nuevaPregunta = Question(
                        cuestionarioId = 0,
                        text = pregunta,
                        alternativa1 = alternativa1,
                        alternativa2 = alternativa2,
                        alternativa3 = alternativa3,
                        correctAnswerIndex = respuestaCorrecta
                    )

                    // Agrega la pregunta a la lista
                    preguntas.add(nuevaPregunta)

                    // Limpia los campos de la pregunta
                    pregunta = ""
                    alternativa1 = ""
                    alternativa2 = ""
                    alternativa3 = ""
                    respuestaCorrecta = 0

                    mensaje = "Pregunta agregada correctamente."

                } else {
                    mensaje =
                        "Completa todos los campos y selecciona la respuesta correcta."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text("Agregar pregunta")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Muestra el mensaje de confirmación o error
        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Verde
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Preguntas agregadas: ${preguntas.size}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        // Muestra las preguntas agregadas
        preguntas.forEachIndexed { indice, preguntaGuardada ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = "${indice + 1}. ${preguntaGuardada.text}\n" +
                            "1. ${preguntaGuardada.alternativa1}\n" +
                            "2. ${preguntaGuardada.alternativa2}\n" +
                            "3. ${preguntaGuardada.alternativa3}\n" +
                            "Respuesta correcta: " +
                            "${preguntaGuardada.correctAnswerIndex}",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Guarda el cuestionario y sus preguntas en Room
        Button(
            onClick = {

                if (titulo.isNotBlank() && preguntas.isNotEmpty()) {

                    scope.launch {

                        try {

                            // Guarda el cuestionario y obtiene su ID
                            val idCuestionario =
                                cuestionarioDAO.insertarCuestionario(
                                    Cuestionario(
                                        titulo = titulo.trim()
                                    )
                                ).toInt()

                            // Guarda las preguntas asociadas al cuestionario
                            preguntas.forEach { preguntaGuardada ->

                                cuestionarioDAO.insertarPregunta(
                                    Question(
                                        cuestionarioId = idCuestionario,
                                        text = preguntaGuardada.text,
                                        alternativa1 = preguntaGuardada.alternativa1,
                                        alternativa2 = preguntaGuardada.alternativa2,
                                        alternativa3 = preguntaGuardada.alternativa3,
                                        correctAnswerIndex =
                                            preguntaGuardada.correctAnswerIndex,
                                        explanation =
                                            preguntaGuardada.explanation
                                    )
                                )
                            }

                            // Limpia los campos después de guardar
                            titulo = ""
                            preguntas.clear()

                            mensaje =
                                "Cuestionario guardado correctamente."

                        } catch (e: Exception) {

                            mensaje =
                                "No se pudo guardar el cuestionario."
                        }
                    }

                } else {

                    mensaje =
                        "Escribe un título y agrega al menos una pregunta."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text("Guardar cuestionario")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Vuelve al menú del profesor
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text("Volver")
        }
    }
}
