package cl.duoc.esfocar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.esfocar.data.CuestionarioDAO
import cl.duoc.esfocar.data.model.Question
import cl.duoc.esfocar.data.repository.QuizRepository
import kotlinx.coroutines.launch

// ViewModel contiene la lógica y el estado del cuestionario.
// No dibuja botones ni otros elementos visuales.
class QuizViewModel : ViewModel() {

    // Lista de preguntas que se obtendrán desde Room.
    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    // Indica si las preguntas se están cargando.
    var cargando by mutableStateOf(false)
        private set

    // Guarda un mensaje para informar errores o resultados.
    var mensaje by mutableStateOf("")
        private set

    // Guarda la posición de la pregunta actual.
    var currentQuestionIndex by mutableStateOf(0)
        private set

    // Guarda la alternativa seleccionada.
    // null significa que todavía no se ha seleccionado ninguna.
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set

    // Indica si la respuesta ya fue comprobada.
    var answerChecked by mutableStateOf(false)
        private set

    // Devuelve la pregunta actual o null si no existe.
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    // Carga desde Room las preguntas de un cuestionario.
    fun cargarPreguntas(
        cuestionarioId: Int,
        cuestionarioDAO: CuestionarioDAO
    ) {
        viewModelScope.launch {

            cargando = true
            mensaje = ""

            try {
                // El repositorio consulta las preguntas guardadas.
                val repository = QuizRepository(cuestionarioDAO)

                questions = repository.getQuestions(cuestionarioId)

                // Reinicia el cuestionario.
                currentQuestionIndex = 0
                selectedAnswer = null
                answerChecked = false

                if (questions.isEmpty()) {
                    mensaje = "Este cuestionario no tiene preguntas."
                }

            } catch (e: Exception) {
                questions = emptyList()
                mensaje = "No se pudieron cargar las preguntas."
            } finally {
                cargando = false
            }
        }
    }

    // Registra la alternativa seleccionada por el estudiante.
    fun selectAnswer(index: Int) {

        // No permite cambiar la respuesta después de comprobarla.
        if (!answerChecked) {

            // La pregunta tiene tres alternativas.
            if (index in 0..2) {
                selectedAnswer = index
            }
        }
    }

    // Comprueba la respuesta seleccionada.
    fun checkAnswer() {

        if (selectedAnswer != null && currentQuestion != null) {
            answerChecked = true
        }
    }

    // Indica si la respuesta seleccionada es correcta.
    val isAnswerCorrect: Boolean
        get() {
            val pregunta = currentQuestion

            return pregunta != null &&
                    selectedAnswer == pregunta.correctAnswerIndex - 1
        }

    // Avanza a la siguiente pregunta.
    fun nextQuestion() {

        if (
            answerChecked &&
            currentQuestionIndex < questions.lastIndex
        ) {
            currentQuestionIndex++
            selectedAnswer = null
            answerChecked = false
        }
    }

    // Reinicia el cuestionario desde la primera pregunta.
    fun restartQuiz() {
        currentQuestionIndex = 0
        selectedAnswer = null
        answerChecked = false
    }
}
