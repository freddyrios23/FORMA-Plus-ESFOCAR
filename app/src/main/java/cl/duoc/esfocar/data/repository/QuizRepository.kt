
package cl.duoc.esfocar.data.repository

import cl.duoc.esfocar.data.model.Question

// Repository proporciona las preguntas a la lógica del cuestionario.
// Por ahora los datos están guardados en memoria, no en una base de datos.
class QuizRepository {

    // Lista de preguntas de demostración.
    private val questions = listOf(
        Question(
            id = 1,
            text = "¿Qué estrategia ayuda a reforzar el aprendizaje?",
            options = listOf(
                "Memorizar sin comprender.",
                "Estudiar, practicar y revisar los errores.",
                "Responder sin leer las preguntas."
            ),
            correctAnswerIndex = 1,
            explanation =
                "Practicar y revisar los errores ayuda a reforzar el aprendizaje."
        ),
        Question(
            id = 2,
            text = "¿Qué conviene hacer cuando no comprendes un contenido?",
            options = listOf(
                "Ignorarlo.",
                "Revisarlo y buscar una explicación.",
                "Responder al azar."
            ),
            correctAnswerIndex = 1,
            explanation =
                "Revisar el contenido ayuda a identificar y resolver dudas."
        )
    )

    // Devuelve la lista para que el ViewModel pueda utilizarla.
    fun getQuestions(): List<Question> {
        return questions
    }
}
