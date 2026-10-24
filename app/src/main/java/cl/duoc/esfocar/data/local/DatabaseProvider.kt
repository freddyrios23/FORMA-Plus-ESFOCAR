package cl.duoc.esfocar.data.local

import android.content.Context
import androidx.room.Room

// Proporciona una instancia de la base de datos de FORMA+.
object DatabaseProvider {

    // Crea la base de datos si todavía no existe.
    fun obtenerDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "forma_database"
        ).build()
    }
}