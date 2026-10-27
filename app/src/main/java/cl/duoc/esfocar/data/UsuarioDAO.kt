package cl.duoc.esfocar.data

// Importaciones necesarias para trabajar con Room.
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Modelo que representa a cada usuario.
import cl.duoc.esfocar.data.model.Usuario

// Operaciones para acceder y administrar los usuarios.
@Dao
interface UsuarioDAO {

    // Inserta un nuevo usuario.
    @Insert
    suspend fun insertar(usuario: Usuario)

    // Busca un usuario para iniciar sesión.
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

    // Obtiene las cuentas de los profesores.
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
