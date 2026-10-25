
package cl.duoc.esfocar.data

// Importa las anotaciones necesarias para trabajar con Room.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Importa el modelo que representa a cada usuario.
import cl.duoc.esfocar.data.model.Usuario

// Indica que esta interfaz contiene operaciones
// para acceder a la tabla de usuarios.
@Dao
interface UsuarioDAO {

    // Inserta un nuevo usuario en la tabla "usuarios".
    // suspend permite ejecutar la operación desde una corrutina.
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Busca un usuario cuyo correo y contraseña coincidan.
    // Devuelve el usuario completo, incluido su rol, o null si no existe.
    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo AND password = :password
        LIMIT 1
    """)
    suspend fun buscarUsuario(
        correo: String,
        password: String
    ): Usuario?

    // Busca una cuenta por su correo electrónico.
    // Se utiliza para evitar registros con correos duplicados.
    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo
        LIMIT 1
    """)
    suspend fun buscarPorCorreo(correo: String): Usuario?

    // Comprueba si ya existe una cuenta con el rol ADMIN.
    // Devuelve true si existe al menos una cuenta de administrador.
    // Esto permite evitar crear administradores iniciales duplicados.
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM usuarios
            WHERE rol = 'ADMIN'
        )
    """)
    suspend fun existeAdministrador(): Boolean
}
