
package cl.duoc.esfocar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.esfocar.data.model.Question
import cl.duoc.esfocar.data.repository.QuizRepository

// ViewModel contiene la lógica y el estado del cuestionario.
// No dibuja botones ni otros elementos visuales.
class QuizViewModel : ViewModel() {

    // Repository nos entrega las preguntas.
    private val repository = QuizRepository()

    // Cargamos las preguntas disponibles al crear el ViewModel.
    val questions: List<Question> = repository.getQuestions()

    // Guarda la posición de la pregunta actual.
    // private set permite leerla desde fuera, pero no modificarla.
    var currentQuestionIndex by mutableStateOf(0)
        private set

    // Guarda la posición de la alternativa seleccionada.
    // null significa que el estudiante aún no ha respondido.
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set

    // Indica si el estudiante ya comprobó su respuesta.
    var answerChecked by mutableStateOf(false)
        private set

    // Devuelve la pregunta actual o null si no existe.
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    // Registra la alternativa que el estudiante selecciona.
    fun selectAnswer(index: Int) {
        // No dejamos cambiar la selección después de corregir.
        if (!answerChecked) {
            // Solo aceptamos índices válidos de la pregunta actual.
            val cantidadAlternativas = currentQuestion?.options?.size ?: 0

            if (index in 0 until cantidadAlternativas) {
                selectedAnswer = index
            }
        }
    }

    // Comprueba la respuesta seleccionada.
    fun checkAnswer() {
        // Solo corregimos cuando existe una selección.
        if (selectedAnswer != null && currentQuestion != null) {
            answerChecked = true
        }
    }

    // Informa si la alternativa seleccionada es correcta.
    val isAnswerCorrect: Boolean
        get() {
            val pregunta = currentQuestion

            return pregunta != null &&
                    selectedAnswer == pregunta.correctAnswerIndex
        }

    // Avanza una pregunta y reinicia la selección y la corrección.
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

    // Permite volver a empezar el cuestionario.
    fun restartQuiz() {
        currentQuestionIndex = 0
        selectedAnswer = null
        answerChecked = false
    }
}
