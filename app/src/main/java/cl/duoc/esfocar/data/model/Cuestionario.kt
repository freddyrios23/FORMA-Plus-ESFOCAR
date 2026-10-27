package cl.duoc.esfocar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa la tabla de cuestionarios.
@Entity(tableName = "cuestionarios")
data class Cuestionario(

    // Identificador único generado automáticamente.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Nombre del cuestionario escrito por el profesor.
    val titulo: String
)
