package cl.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.esfocar.data.model.Modulo

@Dao
interface ModuloDAO {

    // Guarda un módulo.
    @Insert
    suspend fun insertar(modulo: Modulo)

    // Obtiene todos los módulos guardados.
    @Query("SELECT * FROM modulos ORDER BY id ASC")
    suspend fun obtenerModulos(): List<Modulo>

    // Elimina un módulo seleccionado.
    @Delete
    suspend fun eliminar(modulo: Modulo)
}