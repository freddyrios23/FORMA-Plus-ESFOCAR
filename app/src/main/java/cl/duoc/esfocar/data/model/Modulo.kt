package cl.duoc.esfocar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa la tabla de módulos en Room.
@Entity(tableName = "modulos")
data class Modulo(

    // Identificador único de cada módulo.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Nombre del módulo.
    val nombre: String
)
