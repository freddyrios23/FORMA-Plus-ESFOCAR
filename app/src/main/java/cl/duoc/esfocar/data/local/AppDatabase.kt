package cl.duoc.esfocar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duoc.esfocar.data.UsuarioDAO
import cl.duoc.esfocar.data.model.Usuario

// Define la base de datos de FORMA+.
@Database(
    entities = [Usuario::class], // Tablas que tendrá la base de datos.
    version = 1                  // Versión inicial de la base de datos.
)
abstract class AppDatabase : RoomDatabase() {

    // Permite acceder a las operaciones del DAO.
    abstract fun usuarioDAO(): UsuarioDAO
}