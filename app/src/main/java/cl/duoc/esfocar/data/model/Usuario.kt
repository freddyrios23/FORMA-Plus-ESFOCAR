
package cl.duoc.esfocar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Indica que esta clase se almacenará en una tabla
// llamada "usuarios" dentro de la base de datos.
@Entity(tableName = "usuarios")
data class Usuario(

    // Identificador único de cada usuario.
    // Room genera el número automáticamente al insertar un usuario.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Guarda el nombre del usuario.
    val nombre: String,

    // Guarda el correo electrónico que utilizará para iniciar sesión.
    val correo: String,

    // Guarda la contraseña del usuario.
    // IMPORTANTE: actualmente se almacena como texto plano;
    // posteriormente debemos implementar un almacenamiento seguro.
    val password: String,

    // Indica el tipo de usuario que tiene la cuenta.
    // ESTUDIANTE: rol asignado por defecto al registrarse.
    // PROFESOR: cuenta creada o autorizada por el administrador.
    // ADMIN: cuenta con permisos para gestionar profesores.
    val rol: String = "ESTUDIANTE"
)
