package cl.duoc.esfocar.data.local

import android.content.Context
import androidx.room.Room

// Proporciona una instancia de la base de datos de ESFOCAR.
object DatabaseProvider {

    // Crea la base de datos si no existe
    // o abre la que ya está guardada.
    fun obtenerDatabase(context: Context): AppDatabase {

        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "forma_database"
        )
            // Registra todas las migraciones.
            .addMigrations(
                AppDatabase.MIGRATION_1_2,
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4,
                AppDatabase.MIGRATION_4_5
            )
            .build()
    }
}
