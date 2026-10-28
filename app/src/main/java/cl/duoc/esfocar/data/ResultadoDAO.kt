package cl.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.esfocar.data.model.ResultadoCuestionario

@Dao
interface ResultadoDAO {

    // Guarda el resultado de un alumno.
    @Insert
    suspend fun guardarResultado(resultado: ResultadoCuestionario)

    // Obtiene todos los resultados, mostrando primero los más recientes.
    @Query("SELECT * FROM resultados_cuestionarios ORDER BY fecha DESC")
    suspend fun obtenerResultados(): List<ResultadoCuestionario>
}
