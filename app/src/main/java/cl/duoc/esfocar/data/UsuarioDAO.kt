
package cl.duoc.esfocar.data

// Anotaciones para definir las consultas de Room.
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Modelo de usuario almacenado en la base de datos.
import cl.duoc.esfocar.data.model.Usuario

// Operaciones disponibles para administrar usuarios.
@Dao
interface UsuarioDAO {

    // Crea una cuenta nueva.
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Busca una cuenta para iniciar sesión.
    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo AND password = :password
        LIMIT 1
    """)
    suspend fun buscarUsuario(
        correo: String,
        password: String
    ): Usuario?

    // Busca una cuenta por su correo.
    @Query("""
        SELECT * FROM usuarios
        WHERE correo = :correo
        LIMIT 1
    """)
    suspend fun buscarPorCorreo(correo: String): Usuario?

    // Comprueba si ya existe un administrador.
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM usuarios
            WHERE rol = 'ADMIN'
        )
    """)
    suspend fun existeAdministrador(): Boolean

    // Obtiene solamente las cuentas de profesores.
    @Query("""
        SELECT * FROM usuarios
        WHERE rol = 'PROFESOR'
        ORDER BY nombre ASC
    """)
    suspend fun obtenerProfesores(): List<Usuario>

    // Actualiza los datos de un usuario existente.
    @Update
    suspend fun actualizar(usuario: Usuario)

    // Elimina un usuario específico.
    @Delete
    suspend fun eliminar(usuario: Usuario)

    // Comprueba si otro usuario ya utiliza ese correo.
    // Excluye el ID del profesor que se está editando.
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM usuarios
            WHERE correo = :correo AND id != :id
        )
    """)
    suspend fun correoExisteEnOtroUsuario(
        correo: String,
        id: Int
    ): Boolean
}
