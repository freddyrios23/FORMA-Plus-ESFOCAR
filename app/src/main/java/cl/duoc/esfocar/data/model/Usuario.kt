package cl.duoc.esfocar.data.model

// Permite importar las herramientas de Room para trabajar con SQLite.
import androidx.room.Entity
import androidx.room.PrimaryKey

// Indica que esta clase se convertirá en una tabla llamada "usuarios".
@Entity(tableName = "usuarios")
data class Usuario(

    // Identificador único de cada usuario.
    // Room genera el ID automáticamente al guardar un registro.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Nombre del usuario.
    val nombre: String,

    // Correo electrónico del usuario.
    val correo: String,

    // Contraseña del usuario.
    // En una aplicación real no debe almacenarse en texto plano.
    val password: String
)