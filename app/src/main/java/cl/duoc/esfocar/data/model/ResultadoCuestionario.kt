package cl.duoc.esfocar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resultados_cuestionarios")
data class ResultadoCuestionario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombreAlumno: String,
    val correoAlumno: String,
    val cuestionarioId: Int,
    val tituloCuestionario: String,
    val respuestasCorrectas: Int,
    val totalPreguntas: Int,
    val porcentaje: Int,
    val fecha: Long = System.currentTimeMillis()
)
