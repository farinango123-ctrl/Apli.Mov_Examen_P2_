package com.example.techaudit20.data

import android.util.Log
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import com.example.techaudit20.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDao) {
    val allItem: Flow<List<AuditItem>> = auditDao.getAllItems()

    suspend fun insert(item: AuditItem) {
        auditDao.insert(item)
    }

    suspend fun update(item: AuditItem) {
        auditDao.update(item)
    }

    suspend fun delete(item: AuditItem) {
        auditDao.delete(item)
    }

    suspend fun sincronizarTodo(laboratorios: List<Laboratorio>, equipos: List<AuditItem>): Boolean {
        var exitoGlobal = true
        return try {
            // Sincronizar cada laboratorio
            laboratorios.forEach { lab ->
                try {
                    val response = RetrofitClient.api.enviarLaboratorio(lab)
                    if (!response.isSuccessful) {
                        Log.e("SYNC", "Error al enviar laboratorio ${lab.nombre}: ${response.code()} ${response.errorBody()?.string()}")
                        exitoGlobal = false
                    } else {
                        Log.d("SYNC", "Laboratorio enviado: ${lab.nombre}")
                    }
                } catch (e: Exception) {
                    Log.e("SYNC", "Excepción laboratorio ${lab.nombre}: ${e.message}")
                    exitoGlobal = false
                }
            }
            
            // Sincronizar cada equipo
            equipos.forEach { equipo ->
                try {
                    val response = RetrofitClient.api.enviarEquipo(equipo)
                    if (!response.isSuccessful) {
                        Log.e("SYNC", "Error al enviar equipo ${equipo.nombre}: ${response.code()} ${response.errorBody()?.string()}")
                        exitoGlobal = false
                    } else {
                        Log.d("SYNC", "Equipo enviado: ${equipo.nombre}")
                    }
                } catch (e: Exception) {
                    Log.e("SYNC", "Excepción equipo ${equipo.nombre}: ${e.message}")
                    exitoGlobal = false
                }
            }
            exitoGlobal
        } catch (e: Exception) {
            Log.e("SYNC", "Fallo catastrófico en sincronización: ${e.message}")
            false
        }
    }
}
