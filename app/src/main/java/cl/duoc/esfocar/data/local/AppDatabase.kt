package cl.duoc.esfocar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import cl.duoc.esfocar.data.UsuarioDAO
import cl.duoc.esfocar.data.ModuloDAO
import cl.duoc.esfocar.data.model.Usuario
import cl.duoc.esfocar.data.model.Modulo
import cl.duoc.esfocar.data.CuestionarioDAO
import cl.duoc.esfocar.data.model.Cuestionario
import cl.duoc.esfocar.data.model.Question

@Database(
    entities = [
        Usuario::class,
        Modulo::class,
        Cuestionario::class,
        Question::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    // Acceso a las operaciones de usuarios.
    abstract fun usuarioDAO(): UsuarioDAO

    // Acceso a las operaciones de módulos.
    abstract fun moduloDAO(): ModuloDAO

    // Permite acceder a las operaciones de cuestionarios y preguntas.
    abstract fun cuestionarioDAO(): CuestionarioDAO

    companion object {

        // Migración de la versión 1 a la 2.
        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE usuarios
                    ADD COLUMN rol TEXT NOT NULL DEFAULT 'ESTUDIANTE'
                    """
                )
            }
        }

        // Migración de la versión 2 a la 3.
        val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `modulos` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `nombre` TEXT NOT NULL
                    )
                    """
                )
            }
        }

        // Migración de la versión 3 a la 4.
        val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(db: SupportSQLiteDatabase) {

                // Crea la tabla de cuestionarios.
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `cuestionarios` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `titulo` TEXT NOT NULL
            )
            """
                )

                // Crea la tabla de preguntas.
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `preguntas` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `cuestionarioId` INTEGER NOT NULL,
                `text` TEXT NOT NULL,
                `alternativa1` TEXT NOT NULL,
                `alternativa2` TEXT NOT NULL,
                `alternativa3` TEXT NOT NULL,
                `correctAnswerIndex` INTEGER NOT NULL,
                `explanation` TEXT NOT NULL
            )
            """
                )
            }
        }
    }
}