package cl.duoc.esfocar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa la tabla de preguntas.
@Entity(tableName = "preguntas")
data class Question(

    // Identificador único de la pregunta.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Identificador del cuestionario al que pertenece.
    val cuestionarioId: Int,

    // Texto que verá el estudiante.
    val text: String,

    // Alternativas disponibles.
    val alternativa1: String,
    val alternativa2: String,
    val alternativa3: String,

    // Índice de la alternativa correcta: 0, 1 o 2.
    val correctAnswerIndex: Int,

    // Explicación de la respuesta.
    val explanation: String = ""
)
