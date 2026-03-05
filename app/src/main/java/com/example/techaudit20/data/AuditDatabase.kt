package com.example.techaudit20.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio

@Database(entities = [AuditItem::class, Laboratorio::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AuditDatabase : RoomDatabase() {
    abstract fun auditDao(): AuditDao
    abstract fun labDao(): LaboratorioDao

    companion object {
        @Volatile
        private var INSTANCE: AuditDatabase? = null

        fun getDatabase(context: Context): AuditDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuditDatabase::class.java,
                    "techaudit_database"
                )
                .fallbackToDestructiveMigration() // Útil durante el desarrollo si cambias el esquema
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
