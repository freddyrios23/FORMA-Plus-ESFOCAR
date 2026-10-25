package cl.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.esfocar.data.model.Cuestionario
import cl.duoc.esfocar.data.model.Question

@Dao
interface CuestionarioDAO {

    // Guarda un cuestionario y devuelve su ID.
    @Insert
    suspend fun insertarCuestionario(
        cuestionario: Cuestionario
    ): Long

    // Obtiene todos los cuestionarios guardados.
    @Query("SELECT * FROM cuestionarios ORDER BY id ASC")
    suspend fun obtenerCuestionarios(): List<Cuestionario>

    // Guarda una pregunta.
    @Insert
    suspend fun insertarPregunta(pregunta: Question)

    // Obtiene las preguntas de un cuestionario específico.
    @Query(
        "SELECT * FROM preguntas WHERE cuestionarioId = :cuestionarioId ORDER BY id ASC"
    )
    suspend fun obtenerPreguntas(
        cuestionarioId: Int
    ): List<Question>
}
