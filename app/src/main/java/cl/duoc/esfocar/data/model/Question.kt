
package cl.duoc.esfocar.data.model

// Representa una pregunta dentro de FORMA+.
// Cada propiedad guarda un dato que necesitamos para mostrarla y corregirla.
data class Question(
    val id: Int,                       // Identificador de la pregunta.
    val text: String,                  // Enunciado que verá el estudiante.
    val options: List<String>,         // Alternativas disponibles.
    val correctAnswerIndex: Int,       // Índice de la alternativa correcta.
    val explanation: String            // Explicación del resultado.
)
