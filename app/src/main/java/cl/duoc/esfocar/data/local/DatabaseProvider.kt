
package cl.duoc.esfocar.data.local

// Permite acceder al contexto de la aplicación Android.
import android.content.Context

// Importa Room para crear y configurar la base de datos.
import androidx.room.Room

// Proporciona una instancia de la base de datos de ESFOCAR.
object DatabaseProvider {

    // Crea la base de datos si no existe
    // o abre la que ya está guardada.
    fun obtenerDatabase(context: Context): AppDatabase {

        return Room.databaseBuilder(
            context.applicationContext, // Usa el contexto de la aplicación.
            AppDatabase::class.java,    // Indica qué base de datos se utilizará.
            "forma_database"            // Conserva el nombre de la base de datos actual.
        )
            // Registra la migración de la versión 1 a la 2.
            // Así se conserva la información de los usuarios existentes.
            .addMigrations(
                AppDatabase.MIGRATION_1_2,
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4
            )

            // Construye y devuelve la instancia de la base de datos.
            .build()
    }
}
