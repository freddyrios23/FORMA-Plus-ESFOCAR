package cl.duoc.esfocar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.esfocar.data.local.DatabaseProvider
import cl.duoc.esfocar.viewmodel.QuizViewModel

@Composable
fun ResponderCuestionarioScreen(
    cuestionarioId: Int,
    onVolver: () -> Unit
) {
    val contexto = LocalContext.current

    // Obtenemos la base de datos y el acceso a los cuestionarios.
    val cuestionarioDAO = remember {
        DatabaseProvider.obtenerDatabase(contexto).cuestionarioDAO()
    }

    // Usamos el ViewModel para manejar las preguntas y respuestas.
    val viewModel: QuizViewModel = viewModel()

    // Cargamos las preguntas del cuestionario seleccionado.
    LaunchedEffect(cuestionarioId) {
        viewModel.cargarPreguntas(cuestionarioId, cuestionarioDAO)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Responder cuestionario",
            fontSize = 24.sp
        )

        when {
            viewModel.cargando -> {
                Text("Cargando preguntas...")
            }

            viewModel.questions.isEmpty() -> {
                Text(
                    text = viewModel.mensaje.ifBlank {
                        "Este cuestionario no tiene preguntas."
                    }
                )

                Button(onClick = onVolver) {
                    Text("Volver a cuestionarios")
                }
            }

            else -> {
                val pregunta = viewModel.currentQuestion

                if (pregunta != null) {
                    Text(
                        text = "Pregunta ${viewModel.currentQuestionIndex + 1} de ${viewModel.questions.size}",
                        fontSize = 18.sp
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = pregunta.text,
                                fontSize = 20.sp
                            )

                            val alternativas = listOf(
                                pregunta.alternativa1,
                                pregunta.alternativa2,
                                pregunta.alternativa3
                            )

                            alternativas.forEachIndexed { index, alternativa ->
                                androidx.compose.foundation.layout.Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected = viewModel.selectedAnswer == index,
                                        onClick = {
                                            viewModel.selectAnswer(index)
                                        },
                                        enabled = !viewModel.answerChecked
                                    )

                                    Text(
                                        text = alternativa,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
                                }
                            }

                            if (viewModel.answerChecked) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (viewModel.isAnswerCorrect) {
                                        "¡Respuesta correcta!"
                                    } else {
                                        "Respuesta incorrecta."
                                    }
                                )

                                if (pregunta.explanation.isNotBlank()) {
                                    Text(text = pregunta.explanation)
                                }
                            }
                        }
                    }

                    if (!viewModel.answerChecked) {
                        Button(
                            onClick = { viewModel.checkAnswer() },
                            enabled = viewModel.selectedAnswer != null
                        ) {
                            Text("Comprobar respuesta")
                        }
                    } else if (
                        viewModel.currentQuestionIndex < viewModel.questions.lastIndex
                    ) {
                        Button(
                            onClick = { viewModel.nextQuestion() }
                        ) {
                            Text("Siguiente pregunta")
                        }
                    } else {
                        Text("¡Has terminado el cuestionario!")

                        Button(onClick = onVolver) {
                            Text("Volver a cuestionarios")
                        }
                    }
                }
            }
        }
    }
}
