package com.example.techaudit20.uil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.TechAuditApp
import com.example.techaudit20.data.AuditRepository
import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuditRepository
    val allItems: LiveData<List<AuditItem>>

    init {
        val database = (application as TechAuditApp).database
        val auditDao = database.auditDao()
        val labDao = database.labDao()
        repository = AuditRepository(auditDao)
        allItems = repository.allItem.asLiveData()

        // Creamos un laboratorio por defecto si no existe para evitar errores de ForeignKey
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                labDao.insert(Laboratorio(id = "LAB_GENERAL", nombre = "Laboratorio General"))
            }
        }
    }

    fun insert(item: AuditItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: AuditItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }
}
