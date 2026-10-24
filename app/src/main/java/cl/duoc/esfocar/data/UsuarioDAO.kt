package cl.duoc.esfocar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.duoc.esfocar.data.model.Usuario

// Indica que esta interfaz contiene operaciones para acceder a la base de datos.
@Dao
interface UsuarioDAO {

    // Guarda un usuario en la tabla "usuarios".
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Busca un usuario que tenga el correo y la contraseña indicados.
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    suspend fun buscarUsuario(correo: String, password: String): Usuario?

    // Busca si ya existe una cuenta con ese correo.
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}