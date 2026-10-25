package cl.duoc.esfocar.data.repository

import cl.duoc.esfocar.data.CuestionarioDAO
import cl.duoc.esfocar.data.model.Question

// Repository obtiene las preguntas desde la base de datos.
class QuizRepository(
    private val cuestionarioDAO: CuestionarioDAO
) {

    // Obtiene las preguntas del cuestionario seleccionado.
    suspend fun getQuestions(cuestionarioId: Int): List<Question> {

        // Consulta Room y devuelve las preguntas guardadas.
        return cuestionarioDAO.obtenerPreguntas(cuestionarioId)
    }
}
