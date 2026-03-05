package com.example.techaudit20.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.flow.Flow

@Dao
interface LaboratorioDao {
    @Query("SELECT * FROM laboratorios")
    fun getAllLabs(): Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)suspend fun insert(lab: Laboratorio)

    @Delete
    suspend fun delete(lab: Laboratorio)
}