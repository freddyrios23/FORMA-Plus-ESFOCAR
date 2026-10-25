
package cl.duoc.esfocar.data.local

// Importa las anotaciones necesarias para configurar Room.
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Importa el DAO que contiene las consultas de los usuarios.
import cl.duoc.esfocar.data.UsuarioDAO

// Importa el modelo que representa la tabla de usuarios.
import cl.duoc.esfocar.data.model.Usuario

// Define la base de datos de ESFOCAR.
@Database(
    entities = [Usuario::class], // Indica las tablas que utiliza la base de datos.
    version = 2                  // Aumenta la versión porque agregamos el campo rol.
)
abstract class AppDatabase : RoomDatabase() {

    // Permite acceder a las operaciones para insertar y buscar usuarios.
    abstract fun usuarioDAO(): UsuarioDAO

    companion object {

        // Migra la base de datos de la versión 1 a la versión 2.
        // Agrega la columna rol a los usuarios que ya estaban registrados.
        // Los usuarios existentes recibirán el rol ESTUDIANTE por defecto.
        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {

                // Agrega la columna rol a la tabla usuarios.
                // No elimina los datos que ya estaban almacenados.
                db.execSQL(
                    """
                    ALTER TABLE usuarios
                    ADD COLUMN rol TEXT NOT NULL DEFAULT 'ESTUDIANTE'
                    """
                )
            }
        }
    }
}
